package game.engine;

public class GameEngine {
    private GameManager gameManager;

    public GameEngine() {
        gameManager = new GameManager();
    }

    public void start() {
        while (!gameManager.gameIsOver()) {
            gameManager.start();
        }
    }
}
