package ru.tinkoff.cake.data;

import android.support.annotation.NonNull;

public class Score implements Comparable<Score> {

    private String name;

    private int score;

    public Score(String name, int score) {
        this.name = name;
        this.score = score;
    }

    public String getName() {
        return name;
    }

    public int getScore() {
        return score;
    }

    @Override
    public String toString() {
        return name + ": " + score;
    }

    @Override
    public int compareTo(@NonNull  Score another) {
        return this.score < another.score ? -1 : (this.score == another.score ? 0 : 1);
    }
}
