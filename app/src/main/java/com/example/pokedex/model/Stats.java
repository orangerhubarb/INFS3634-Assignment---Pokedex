package com.example.pokedex.model;

/**
 * Created by Davian on 27/09/16.
 */
public class Stats {
    private int speed;
    private int spdef;
    private int spatk;
    private int def;
    private int att;
    private int hp;

    public Stats(int hp, int att, int def, int spatk, int spdef, int speed) {
        this.speed = speed;
        this.spdef = spdef;
        this.spatk = spatk;
        this.def = def;
        this.att = att;
        this.hp = hp;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public void setSpdef(int spdef) {
        this.spdef = spdef;
    }

    public void setSpatk(int spatk) {
        this.spatk = spatk;
    }

    public void setDef(int def) {
        this.def = def;
    }

    public void setAtt(int att) {
        this.att = att;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public int getSpeed() {

        return speed;
    }

    public int getSpdef() {
        return spdef;
    }

    public int getSpatk() {
        return spatk;
    }

    public int getDef() {
        return def;
    }

    public int getAtt() {
        return att;
    }

    public int getHp() {
        return hp;
    }

    @Override
    public String toString() {
        return "Stats{" +
                "speed=" + speed +
                ", spdef=" + spdef +
                ", spatk=" + spatk +
                ", def=" + def +
                ", att=" + att +
                ", hp=" + hp +
                '}';
    }
}
