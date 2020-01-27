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

    public void start() {
//        dialogue.getDialogue(progress);
        while (progressBossIsAlive()) {
            getUserAction();
        }
    }

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

    private void checkRoomForEnemy() {
        Room room = currentMap.getHeroLocation();
        Unit enemy = room.getEnemy();
        if (enemy == null) return;
        if (enemy.isAlive()) {
            bm.setEnemy(enemy);
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

    private boolean progressBossIsAlive() {
        Room bossRoom = currentMap.getBossRoom();
        return bossRoom.getEnemy().isAlive();
    }

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

    private void checkBossIsAlive() {
        ArrayList<Room> rooms = currentProgress.getOpenedRooms(progress);
        Room room = rooms.get(rooms.size()-1);
        Unit enemyBoss = room.getEnemy();

        if (!enemyBoss.isAlive()) {
            raiseProgress();
        }
    }
}
