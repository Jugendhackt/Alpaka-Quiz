package de.jugendhackt.koeln.alpakaquiz.data;

import com.google.common.collect.HashBiMap;

public class Question {
    final String question = "TestQuestion1";
    final HashBiMap<QuizColors, String> answers = HashBiMap.create();
    final QuizColors correctAnswer = QuizColors.BLUE;

    public Question() {
        answers.put(QuizColors.RED, "ROOOOOT");
        answers.put(QuizColors.BLUE, "BLAAAAAU");
        answers.put(QuizColors.GREEN, "GRÜÜÜÜÜÜÜN");
        answers.put(QuizColors.YELLOW, "GEEEEELB");
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
}
