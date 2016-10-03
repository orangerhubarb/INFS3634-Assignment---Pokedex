package com.example.pokedex.model;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by Davian on 17/09/16.
 */

public class Pokemon implements Serializable {
    private String name;
    private int id;
    private String JSONObject = null;
    private ArrayList<String> abilities;
    private Bitmap sprite;
    private ArrayList<Moves> moves;
    private Stats stats;
    private String flavourText;
    private ArrayList<Evolution> evolutions;
    private ArrayList<String> types;
    private Bitmap bigImage;
    private double height;
    private double weight;
    private String genderRatio;

    public ArrayList<Evolution> getEvolutions() {
        return evolutions;
    }

    public void setEvolutions(ArrayList<Evolution> evolutions) {
        this.evolutions = evolutions;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public String getGenderRatio() {
        return genderRatio;
    }

    public void setGenderRatio(String genderRatio) {
        this.genderRatio = genderRatio;
    }

    public Pokemon() {

    }

    public Pokemon(String name, int id, ArrayList<String> abilities, ArrayList<Evolution> evolution, ArrayList<String> types) {
        this.name = name;
        this.id = id;
        this.abilities = abilities;
        this.evolutions = evolution;
        this.types = types;
    }

    public Pokemon(String name, int id, Bitmap sprite) {
        this.name = name;
        this.id = id;
        this.sprite = sprite;
    }

    public String getJSONObject() {
        return JSONObject;
    }

    public void setJSONObject(String JSONObject) {
        this.JSONObject = JSONObject;
    }


    public String getFlavourText() {
        return flavourText;
    }

    public void setFlavourText(String flavourText) {
        this.flavourText = flavourText;
    }


    public Bitmap getBigImage() {
        return bigImage;
    }

    public void setBigImage(Bitmap bigImage) {
        this.bigImage = bigImage;
    }


    public ArrayList<Moves> getMoves() {
        return moves;
    }

    public void setMoves(ArrayList<Moves> moves) {
        this.moves = moves;
    }

    public Stats getStats() {
        return stats;
    }

    public void setStats(Stats stats) {
        this.stats = stats;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public Bitmap getSprite() {
        return sprite;
    }

    public ArrayList<String> getAbilities() {
        return abilities;
    }


    public void setName(String name) {
        this.name = name;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setSprite(Bitmap sprite) {
        this.sprite = sprite;
    }

    public void setAbilities(ArrayList<String> abilities) {
        this.abilities = abilities;
    }

    public void setEvolution(ArrayList<Evolution> evolution) {
        this.evolutions = evolution;
    }

    public void setTypes(ArrayList<String> types) {
        this.types = types;
    }

    public ArrayList<Evolution> getEvolution() {
        return evolutions;
    }

    public ArrayList<String> getTypes() {
        return types;
    }


    public String toString() {
        return name + " " + id + sprite.toString();
    }

}

