package com.example.pokedex.model;

/**
 * Created by Davian on 27/09/16.
 */
public class Evolution {
    public String name;
    public int levelEvolved;
    public String evolveMethod;

    public Evolution(String name, int levelEvolved, String evolveMethod) {
        this.name = name;
        this.levelEvolved = levelEvolved;
        this.evolveMethod = evolveMethod;
    }

    public Evolution(String name) {
        this.name = name;
    }

    public Evolution(String name, int levelEvolved) {
        this.name = name;
        this.levelEvolved = levelEvolved;

    }


    public String getEvolveMethod() {
        return evolveMethod;
    }

    public void setEvolveMethod(String evolveMethod) {
        this.evolveMethod = evolveMethod;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getLevelEvolved() {
        return levelEvolved;
    }

    public void setLevelEvolved(int levelEvolved) {
        this.levelEvolved = levelEvolved;
    }

    public String toString() {
        return name + evolveMethod + levelEvolved;
    }
}
