package de.jugendhackt.koeln.alpakaquiz.data;

import com.google.common.collect.HashBiMap;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

public class Quiz {
    QuizState state = QuizState.WAITING_FOR_CLIENTS;
    HashBiMap<String, Team> teams = HashBiMap.create();
    ArrayList<Question> askedQuestions = new ArrayList<>();
    ArrayList<Question> questions = new ArrayList<>(Arrays.asList(new Question(), new Question(), new Question()));


    public QuizState getState() {
        return state;
    }

    public HashBiMap<String, Team> getTeams() {
        return teams;
    }

    public ArrayList<Question> getAskedQuestions() {
        return askedQuestions;
    }

    public ArrayList<Question> getQuestions() {
        return questions;
    }
}
