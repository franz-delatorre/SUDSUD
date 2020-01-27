package game.engine;

public class GameEngine implements GameCycle {
    private GameManager gameManager;
    private boolean gameOver;

    public GameEngine() {
        gameManager = new GameManager();
        gameOver    = false;
    }

    @Override
    public void start() {
        while (!gameManager.gameIsOver()) {
            gameManager.start();
        }
        System.out.println("You Completed all levels congratulations");
    }
}
