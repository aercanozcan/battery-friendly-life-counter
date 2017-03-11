package com.apptopus.bflifecounter.model;

/**
 * Created by ercanozcan on 11/03/17.
 */
public class Player {
    private int initLife = 20;
    private int life = initLife;
    private int energy = 0;

    public Player(int life, int energy) {
        this.initLife = life;
        this.life = life;
        this.energy = energy;
    }

    public Player() {
    }

    public int getLife() {
        return life;
    }

    public void setLife(int life) {
        this.life = life;
    }

    public int getEnergy() {
        return energy;
    }

    public void setEnergy(int energy) {
        this.energy = energy;
    }

    public int getInitLife() {
        return initLife;
    }

    public void setInitLife(int initLife) {
        this.initLife = initLife;
    }
}
