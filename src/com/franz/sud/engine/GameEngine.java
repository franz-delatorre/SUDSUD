package com.franz.sud.engine;

import com.franz.sud.cartridge.Cartridge;

import java.util.ArrayList;
import java.util.HashMap;

public class GameEngine {
    private GameManager gameManager;
    private Cartridge game;
    private ArrayList<Cartridge> listOfGames = new ArrayList<>();

    public void addGame(Cartridge game) {
        listOfGames.add(game);
    }

    public GameEngine() {
        gameManager = new GameManager();
    }

    public void start() {
        boolean stillPlaying = true;
        do {
            System.out.println("[C] Select Game");
            System.out.println("[S] Start Game");
            System.out.println("[C] Shutdown");

        } while (stillPlaying);
    }

    private void chooseGame() {
        HashMap<Integer, Cartridge> options = new HashMap<>();
        if (listOfGames.isEmpty()) {
            System.out.println("No Cartridge available");
            return;
        }

        int index = 0;
        for (Cartridge cartridge: listOfGames) {
            options.put(index++, cartridge);
        }
    }

    private void gameStart() {
        game.start();
    }
}
