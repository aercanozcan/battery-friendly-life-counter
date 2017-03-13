package com.apptopus.bflifecounter.game;

import com.apptopus.bflifecounter.model.Player;

import java.util.ArrayList;
import java.util.List;

import co.uk.rushorm.core.RushSearch;

/**
 * Created by ercanozcan on 12/03/17.
 */
public class Game {
    private List<Player> players;
    private static Game instance;
    private int initLife = 20;

    public Game() {
        players = new RushSearch().orderAsc(Player.PLAYER_ID).find(Player.class);
        if (players == null || players.isEmpty()) {
            players = new ArrayList<>();
            players.add(new Player(1, initLife, 0));
            players.add(new Player(2, initLife, 0));
            players.get(0).save();
            players.get(1).save();
        }
    }

    public static Game getInstance() {
        if (instance == null) {
            instance = new Game();
        }
        return instance;
    }

    private Player player(int player) {
        return players.get(player);
    }

    public Player playerOne() {
        return player(0);
    }

    public Player playerTwo() {
        return player(1);
    }

    public void increasePlayerLife(Player player) {
        player.setLife(player.getLife() + 1);
        player.save();
    }

    public void decreasePlayerLife(Player player) {
        player.setLife(player.getLife() - 1);
        player.save();
    }

}
