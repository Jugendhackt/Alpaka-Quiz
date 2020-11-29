package de.jugendhackt.koeln.alpakaquiz.data;

import com.google.gson.JsonObject;

import java.util.HashMap;

public class Question {
    final String question;
    final HashMap<QuizColors, String> answers;
    final QuizColors correctAnswer;
    final JsonObject json = new JsonObject();
    final HashMap<Team, QuizColors> givenAnswers = new HashMap<>();
    boolean asked = false;

    public Question(String question, HashMap<QuizColors, String> answers, QuizColors correctAnswer) {
        this.question = question;
        this.answers = answers;
        this.correctAnswer = correctAnswer;

        answers.forEach(((quizColors, answer) -> json.addProperty(quizColors.name().toLowerCase(), answer)));
    }

    public String getQuestion() {
        return question;
    }

    public HashMap<QuizColors, String> getAnswers() {
        return answers;
    }

    public QuizColors getCorrectAnswer() {
        return correctAnswer;
    }

    public JsonObject getAnswersAsJson() {
        return json;
    }

    public void answerTeam(Team team, QuizColors color) {
        if (!answers.containsKey(color)) {
            return;
        }
        if (givenAnswers.containsKey(team)) {
            return;
        }
        givenAnswers.put(team, color);

        if (color == correctAnswer) {
            team.addScore(500 / givenAnswers.size());
        }
    }

    public HashMap<Team, QuizColors> getGivenAnswers() {
        return givenAnswers;
    }

    public JsonObject getResults() {
        JsonObject json = new JsonObject();
        JsonObject givenAnswersJson = new JsonObject();
        JsonObject allAnswersJson = new JsonObject();
        HashMap<QuizColors, Integer> answers = new HashMap<>();
        this.answers.forEach((color, answer) -> {
            answers.put(color, 0);
            allAnswersJson.addProperty(color.name().toLowerCase(), answer);
        });
        givenAnswers.forEach((team, answer) -> answers.put(answer, answers.get(answer) + 1));

        answers.forEach((color, count) -> givenAnswersJson.addProperty(color.name().toLowerCase(), count));

        json.addProperty("correct", correctAnswer.name().toLowerCase());
        json.add("givenAnswers", givenAnswersJson);
        json.add("allAnswers", allAnswersJson);
        asked = true;
        return json;
    }

    public boolean isAlreadyAsked() {
        return asked;
    }
}
