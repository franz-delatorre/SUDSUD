package com.franz.sud.engine;

import com.franz.sud.cartridge.Cartridge;
import com.franz.sud.misc.GameType;

public class GameEngine {
    private GameManager gameManager;
    private Cartridge game;
    private GameType gameType;

    public GameEngine(GameType gameType) {
        this.gameType = gameType;
        gameManager = new GameManager();
    }

    public void addGame(Cartridge game) {
        if (game.getGameType() == gameType)
        this.game = game;
    }

    private void gameStart() {
        gameManager.setGame(game);
        gameManager.startGame();
    }
}
