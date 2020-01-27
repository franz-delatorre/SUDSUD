package game.engine;

import components.geography.GameMap;
import components.geography.Room;
import components.item.Inventory;
import components.item.Item;
import components.unit.SkilledUnit;
import components.unit.Unit;
import dialogue.Dialogue;
import misc.Direction;

import java.util.ArrayList;
import java.util.Map;
import java.util.Scanner;

public class GameManager {

    private static final Scanner scanner = new Scanner(System.in);

    private Map<String, Item> gameInventory;
    private Inventory heroInventory;
    private int progress;
    private Progress mapOneProgress;
    private Progress mapTwoProgress;
    private Progress currentProgress;
    private SkilledUnit hero;
    private Unit finalBoss;
    private GameMap mapOne;
    private GameMap mapTwo;
    private GameMap currentMap;
    private Room previousRoom;
    private boolean gameOver;
    private Dialogue dialogue;
    private BattleManager bm;

    public GameManager() {
        GameInitializer gi = new GameInitializer();
        gi.initialize();

        gameOver = false;
        progress = 0;
        gameInventory = gi.getItems();
        heroInventory = new Inventory();
        mapOne = gi.getGameMapOne();
        mapTwo = gi.getGameMapTwo();
        mapOneProgress = gi.getMapOneProgress();
        mapTwoProgress = gi.getMapTwoProgress();
        currentProgress = mapOneProgress;
        currentMap = mapOne;
        hero = gi.getHero();
        dialogue = gi.getDialogue();
        bm = new BattleManager(hero);
        previousRoom = currentMap.getHeroLocation();
        finalBoss = gi.getFinalBoss();
    }

    /**
     * Starts the game, will check if there is a dialogue for the progress. Game progress
     * will increment if the current progress boss is slain.
     */
    public void start() {
//        dialogue.getDialogue(progress);
        while (progressBossIsAlive()) {
            getUserAction();
        }
    }

    /**
     * Gets the user's action in the main action UI.
     */
    private void getUserAction() {

        System.out.println("[I] inventory");
        System.out.println("[M] map");
        System.out.println("[Q] quit");
        String opt = scanner.nextLine().toLowerCase();

        switch (opt) {
            case "i":
                break;
            case "m":
                currentMap.showMap();
                move();
                break;
            default:
                System.out.println("Action invalid.");
                getUserAction();
        }
    }

    /**
     * Will move the character around the map. Users cannot move to the adjacent room if
     * it is not listed in the open rooms of the current map used.
     */
    private void move() {
        System.out.println("[e] Exit map");

        Room currRoom = currentMap.getHeroLocation();
        if (currentMap.canMoveToAdjacentRoom(Direction.NORTH, currRoom)) System.out.println("[W] Move up");
        if (currentMap.canMoveToAdjacentRoom(Direction.SOUTH, currRoom)) System.out.println("[S] Move down");
        if (currentMap.canMoveToAdjacentRoom(Direction.EAST, currRoom)) System.out.println("[D] Move right");
        if (currentMap.canMoveToAdjacentRoom(Direction.WEST, currRoom)) System.out.println("[A] Move left");

        switch (scanner.nextLine().toLowerCase()) {
            case "w":
                moveTo(Direction.NORTH);
                break;
            case "s":
                moveTo(Direction.SOUTH);
                break;
            case "a":
                moveTo(Direction.WEST);
                break;
            case "d":
                moveTo(Direction.EAST);
                break;
            case "e":
                return;
            default:
                System.out.println("Wrong input, try again");
                move();
        }
        checkRoomForEnemy();
        currentMap.showMap();
        if (!finalBoss.isAlive()) {
            gameOver = true;
            return;
        }

        move();
    }

    /**
     * Moves the unit to the specified direction
     * @param to
     */
    private void moveTo(Direction to) {
        Room rm = currentMap.getHeroLocation();
        if (currentMap.canMoveToAdjacentRoom(to, rm)) {
            currentMap.setHeroLocation(rm.getAdjacentRoom(to));
            previousRoom = rm;
        }
        else {
            System.out.println("Wrong input, try again");
            move();
        }
    }

    /**
     * Checks if the room has an enemy. If the enemy is alive it will
     * simulate a battle between the hero and the enemy. If the player is defeated
     * he will choose to fight again or not. If the player will not fight on then he
     * will be moved back to the last room he went to.
     */
    private void checkRoomForEnemy() {
        Room room = currentMap.getHeroLocation();
        Unit enemy = room.getEnemy();
        // Checks if the room has an enemy
        if (enemy == null) return;

        //Checks if the enemy is still alive
        if (enemy.isAlive()) {
            bm.setEnemy(enemy);

            //Will check who wins the battle. returns 1 if the player wins and
            // returns zero if otherwise.
            if (bm.toBattle() > 0) {
                System.out.println("You win!! ");
                checkBossIsAlive();
            } else {
                if (fightAgain()) {
                    checkRoomForEnemy();
                } else {
                    currentMap.setHeroLocation(previousRoom);
                }
            }
        }
    }

    public boolean gameIsOver() {
        return gameOver;
    }

    // Checks if the boss of the current progress is still alive
    private boolean progressBossIsAlive() {
        Room bossRoom = currentMap.getBossRoom();
        return bossRoom.getEnemy().isAlive();
    }

    /**
     * Increments the progress if the boss of the current progress is slain.
     */
    private void raiseProgress() {
        if (progress >= 2 && currentMap == mapOne) {
            currentMap = mapTwo;
            progress = 0;
            currentProgress = mapTwoProgress;
            return;
        }

        if (progress >= 2 && currentMap == mapTwo) {
            gameOver = true;
            return;
        }

        ArrayList<Room> newlyOpenedRooms = currentProgress.getOpenedRooms(++progress);
        for (Room room : newlyOpenedRooms) {
            currentMap.addOpenRoom(room);
        }
    }

    /**
     * Asks the user if they will fight again after losing to the enemy.
     * @return
     */
    private boolean fightAgain() {
        System.out.println("You Lost, fight again? [Y] [N]");
        switch (scanner.nextLine().toLowerCase()) {
            case "y":
                return true;
            case "n":
                return false;
            default:
                System.out.println("Wrong input, try again");
                fightAgain();
        }
        return true;
    }

    /**
     * Checks if the progress boss is still alive
     */
    private void checkBossIsAlive() {
        ArrayList<Room> rooms = currentProgress.getOpenedRooms(progress);
        Room room = rooms.get(rooms.size()-1);
        Unit enemyBoss = room.getEnemy();

        if (!enemyBoss.isAlive()) {
            raiseProgress();
        }
    }
}
