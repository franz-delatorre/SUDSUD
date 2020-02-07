package com.franz.sud.engine;

import com.franz.sud.cartridge.Cartridge;

public class GameManager {
    private Cartridge game;

    public void setGame(Cartridge game) {
        this.game = game;
    }

    public void startGame() {
        game.init();
        game.start();
        if (game.isFinished()) {
            System.out.println("Congratulations! You completed the game.");
        } else {
            System.out.println("Game Over!");
        }
    }

}
