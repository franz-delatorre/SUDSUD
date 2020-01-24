package engine;

public class GameEngine implements GameCycle {
    private GameManager gameManager;
    private boolean gameOver;

    public GameEngine() {
        gameManager = new GameManager();
        gameOver    = false;
    }

    @Override
    public void start() {
        while (!gameOver) {
            gameManager.gameProgress();

            gameOver = gameManager.gameIsOver();
        }
    }
}
