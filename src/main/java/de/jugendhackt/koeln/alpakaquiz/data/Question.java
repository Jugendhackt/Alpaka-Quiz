package de.jugendhackt.koeln.alpakaquiz.data;

import com.google.common.collect.HashBiMap;
import com.google.gson.JsonObject;

public class Question {
    final String question = "TestQuestion1";
    final HashBiMap<QuizColors, String> answers = HashBiMap.create();
    final QuizColors correctAnswer = QuizColors.BLUE;
    final JsonObject json = new JsonObject();

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
}
