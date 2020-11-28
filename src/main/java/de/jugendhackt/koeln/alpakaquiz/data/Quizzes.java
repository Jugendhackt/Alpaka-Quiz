package de.jugendhackt.koeln.alpakaquiz.data;

import com.google.common.collect.HashBiMap;

import java.util.Random;

public class Quizzes {
    public static final HashBiMap<Integer, Quiz> QUIZ_PIN_MAP = HashBiMap.create();
    public static final HashBiMap<String, Quiz> QUIZ_ID_MAP = HashBiMap.create();

    public static int createQuiz(Quiz quiz) {
        Random random = new Random();
        int quizPin = -1;
        while (quizPin < 10000 || QUIZ_PIN_MAP.containsKey(quizPin)) {
            quizPin = Math.abs(random.nextInt());
        }
        QUIZ_PIN_MAP.put(quizPin, quiz);
        QUIZ_ID_MAP.put(quiz.getQuizId(), quiz);
        return quizPin;
    }

    public static Quiz getQuizById(String id) {
        return QUIZ_ID_MAP.get(id);
    }
}
