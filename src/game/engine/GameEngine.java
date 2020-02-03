package game.engine;

public class GameEngine implements GameCycle {
    private GameManager gameManager;

    public GameEngine() {
        gameManager = new GameManager();
    }

    @Override
    public void start() {
        while (!gameManager.gameIsOver()) {
            gameManager.start();
        }
    }
}
