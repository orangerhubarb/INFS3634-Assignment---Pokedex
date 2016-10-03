package com.example.pokedex.model;

import java.util.Comparator;

/**
 * Created by Davian on 27/09/16.
 */
public class Moves implements Comparable<Moves> {
    public String name;
    public int levelLearnt;

    public Moves(String name, int levelLearnt) {
        this.name = name;
        this.levelLearnt = levelLearnt;
    }

    @Override
    public String toString() {
        return "Moves{" +
                "name='" + name + '\'' +
                ", levelLearnt='" + levelLearnt + '\'' +
                '}';
    }

    public String getName() {
        return name;
    }

    public int getLevelLearnt() {
        return levelLearnt;
    }

    public void setLevelLearnt(int levelLearnt) {
        this.levelLearnt = levelLearnt;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int compareTo(Moves compareMoves) {
        int compareLevel=(compareMoves).getLevelLearnt();

        return this.levelLearnt-compareLevel;


    }
}
