package com.apptopus.bflifecounter.model;

import co.uk.rushorm.core.RushObject;
import co.uk.rushorm.core.annotations.RushTableAnnotation;

/**
 * Created by ercanozcan on 11/03/17.
 */
@RushTableAnnotation
public class Player extends RushObject {
    public static final String PLAYER_ID = "playerID";
    private int playerID = 1;
    private int life = 20;
    private int energy = 0;

    public Player(int playerID, int life, int energy) {
        this.playerID = playerID;
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

}
