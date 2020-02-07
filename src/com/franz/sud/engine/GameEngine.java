package com.franz.sud.engine;

public class GameEngine {
    private GameManager gameManager;

    public GameEngine() {
        gameManager = new GameManager();
    }

    public void start() {
        gameManager.start();
    }
}
