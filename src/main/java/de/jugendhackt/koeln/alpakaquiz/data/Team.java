package de.jugendhackt.koeln.alpakaquiz.data;

import java.security.Principal;

public class Team {
    final String name;
    final Principal principal;
    int score;

    public Team(String name, Principal principal) {
        this.name = name;
        this.principal = principal;
    }

    public String getName() {
        return name;
    }

    public Principal getPrincipal() {
        return principal;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void increaseScore() {
        score++;
    }

    public void addScore(int score) {
        this.score += score;
    }
}
