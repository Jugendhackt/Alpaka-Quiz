package de.jugendhackt.koeln.alpakaquiz.data;

import com.google.common.collect.HashBiMap;
import com.google.gson.JsonObject;
import de.jugendhackt.koeln.alpakaquiz.socket.JsonSocketSender;
import de.jugendhackt.koeln.alpakaquiz.util.Util;

import java.security.Principal;
import java.util.*;

public class Quiz {
    final Principal whiteboard;
    final String quizId = Util.generateRandomString(128);
    QuizState state = QuizState.WAITING_FOR_CLIENTS;
    HashBiMap<Principal, Team> teams = HashBiMap.create();
    ArrayList<Question> questions = new ArrayList<>(Arrays.asList(new Question(), new Question(), new Question()));
    Question currentQuestion;
    int totalQuestions;

    public Quiz(Principal whiteboard) {
        this.whiteboard = whiteboard;
    }


    public QuizState getState() {
        return state;
    }

    public void setState(QuizState state) {
        this.state = state;
    }

    public HashBiMap<Principal, Team> getTeams() {
        return teams;
    }

    public ArrayList<Question> getQuestions() {
        return questions;
    }

    public void addTeam(Team team) {
        if (teams.size() > 50) {
            throw new RuntimeException("Maximum count of teams exceeded!");
        }
        teams.put(team.getPrincipal(), team);
        JsonObject payload = new JsonObject();
        payload.addProperty("name", team.getName());
        JsonSocketSender.INSTANCE.sendJson(whiteboard, "whiteboard/addTeam", payload);
    }

    public void nextQuestion() {
        if (state == QuizState.WAITING_FOR_ANSWERS || state == QuizState.FINISHED) {
            return;
        }
        if (questions.size() == 0) {
            showFinal();
            return;
        }
        final Question currentQuestion = questions.get(new Random().nextInt(questions.size()));
        this.currentQuestion = currentQuestion;
        new Timer().schedule(
                new TimerTask() {
                    @Override
                    public void run() {
                        if (currentQuestion.isAlreadyAsked()) {
                            return;
                        }
                        showAnswers();
                    }
                },
                20000
        );


        setState(QuizState.WAITING_FOR_ANSWERS);
        questions.remove(currentQuestion);

        JsonObject payload = new JsonObject();
        payload.addProperty("question", currentQuestion.getQuestion());
        // payload.addProperty("time", currentQuestion.getTime());
        payload.add("answers", currentQuestion.getAnswersAsJson());
        payload.addProperty("remainingAnswers", questions.size());
        payload.addProperty("totalQuestions", totalQuestions);

        JsonSocketSender.INSTANCE.sendJson(whiteboard, "whiteboard/question", payload);
        teams.forEach((principal, team) -> JsonSocketSender.INSTANCE.sendJson(principal, "whiteboard/question", payload));
    }

    public void showAnswers() {
        if (state != QuizState.WAITING_FOR_ANSWERS) {
            return;
        }
        setState(QuizState.WAITING_FOR_NEXT_QUESTION);

        JsonObject result = currentQuestion.getResults();

        currentQuestion = null;

        JsonSocketSender.INSTANCE.sendJson(whiteboard, "whiteboard/answers", result);
        teams.forEach((principal, team) -> JsonSocketSender.INSTANCE.sendJson(principal, "whiteboard/answers", result));
    }

    public void showFinal() {
        if (state == QuizState.FINISHED) {
            return;
        }
        setState(QuizState.FINISHED);
        JsonObject json = new JsonObject();
        teams.forEach((principal, team) -> json.addProperty(team.getName(), team.getScore()));

        JsonSocketSender.INSTANCE.sendJson(whiteboard, "whiteboard/finals", json);
        teams.forEach((principal, team) -> JsonSocketSender.INSTANCE.sendJson(principal, "whiteboard/finals", json));
    }

    public void start() {
        nextQuestion();
    }

    public String getQuizId() {
        return quizId;
    }

    public boolean isWhiteboard(Principal sessionId) {
        return sessionId.getName().equals(whiteboard.getName());
    }

    public Team getTeamByPrincipal(Principal principal) {
        return teams.get(principal);
    }

    public Team getTeamByName(String teamName) {
        for (Team team : teams.values()) {
            if (teamName.equals(team.getName())) {
                return team;
            }
        }
        return null;
    }

    public Question getCurrentQuestion() {
        return currentQuestion;
    }
}
