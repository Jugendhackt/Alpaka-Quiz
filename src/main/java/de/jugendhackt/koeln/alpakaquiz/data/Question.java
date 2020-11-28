package de.jugendhackt.koeln.alpakaquiz.data;

import com.google.common.collect.HashBiMap;
import com.google.gson.JsonObject;

import java.util.HashMap;

public class Question {
    final String question = "TestQuestion1";
    final HashBiMap<QuizColors, String> answers = HashBiMap.create();
    final QuizColors correctAnswer = QuizColors.BLUE;
    final JsonObject json = new JsonObject();
    final HashMap<Team, QuizColors> givenAnswers = new HashMap<>();

    public Question() {
        answers.put(QuizColors.RED, "ROOOOOT");
        answers.put(QuizColors.BLUE, "BLAAAAAU");
        answers.put(QuizColors.GREEN, "GRÜÜÜÜÜÜÜN");
        answers.put(QuizColors.YELLOW, "GEEEEELB");


        answers.forEach(((quizColors, answer) -> json.addProperty(quizColors.name().toLowerCase(), answer)));
    }

    public String getQuestion() {
        return question;
    }

    public HashBiMap<QuizColors, String> getAnswers() {
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
    }

    public HashMap<Team, QuizColors> getGivenAnswers() {
        return givenAnswers;
    }

    public JsonObject getResults() {
        JsonObject json = new JsonObject();
        JsonObject answersJson = new JsonObject();
        HashMap<QuizColors, Integer> answers = new HashMap<>();
        this.answers.forEach((color, answer) -> answers.put(color, 0));
        givenAnswers.forEach((team, answer) -> answers.put(answer, answers.get(answer) + 1));

        answers.forEach((color, count) -> answersJson.addProperty(color.name().toLowerCase(), count));

        json.addProperty("correct", correctAnswer.name().toLowerCase());
        json.add("answers", answersJson);
        return json;
    }
}
