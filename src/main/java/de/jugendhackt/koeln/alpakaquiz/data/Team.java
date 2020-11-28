package de.jugendhackt.koeln.alpakaquiz.data;

public class Team {
    final String name;
    int score;

    public Team(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
