package game.engine;

import components.Stats;
import components.geography.GameMap;
import components.geography.Room;
import components.item.Inventory;
import components.item.Item;
import components.unit.SkilledUnit;
import components.unit.Unit;
import dialogue.Dialogue;
import misc.Broadcaster;
import misc.Direction;
import misc.StatType;
import misc.TextColor;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Map;
import java.util.Scanner;

public class GameManager {

    private static final Scanner scanner = new Scanner(System.in);
    private static final String BLACK = TextColor.ANSI_BLACK;
    private static final String CYAN = TextColor.ANSI_CYAN;
    private static final String RED = TextColor.ANSI_RED;
    private static final String PURPLE = TextColor.ANSI_PURPLE;
    private static final String BLUE = TextColor.ANSI_BLUE;
    private static final String GREEN = TextColor.ANSI_GREEN;

    private boolean gameOver;
    private int progress;
    private BattleManager bm;
    private Dialogue dialogue;
    private GameMap map;
    private GameMapProgress gameMapProgress;
    private Inventory heroInventory;
    private Map<String, Item> gameInventory;
    private Room previousRoom;
    private Room secondLocation;
    private SkilledUnit hero;
    private Unit finalBoss;

    public GameManager() {
        GameInitializer gi = new GameInitializer();
        gi.initialize();

        hero = gi.getHero();
        bm = new BattleManager(hero);
        dialogue = gi.getDialogue();
        finalBoss = gi.getFinalBoss();
        gameInventory = gi.getItems();
        gameMapProgress = gi.getGameMapProgress();
        gameOver = false;
        heroInventory = new Inventory();
        map = gi.getGameMap();
        progress = 0;
        secondLocation = gi.getSecondLocation();
    }

    /**
     * Starts the game, will check if there is a dialogue for the progress. Game progress
     * will increment if the current progress boss is slain.
     */
    public void start() {
//        dialogue.getDialogue(progress);
        while (progressBossIsAlive()) {
            if (gameOver) return;
            getUserAction();
        }
    }

    /**
     * Gets the user's action in the main action UI.
     */
    private void getUserAction() {
        if (gameOver) return;

        System.out.println(TextColor.ANSI_BLACK + "[I] Inventory");
        System.out.println("[C] Character");
        System.out.println("[M] Map");
        System.out.println("[Q] Quit");
        String opt = scanner.nextLine().toLowerCase();

        switch (opt) {
            case "i":
                break;
            case "m":
                map.showMap();
                move();
                break;
            case "c":
                showCharacter();
                break;
            default:
                System.out.println("Action invalid.");
                getUserAction();
                break;
            case "q":
                Broadcaster.relayGameOver();
                gameOver = true;
                break;
        }
    }

    /**
     * Will move the character around the map. Users cannot move to the adjacent room if
     * it is not listed in the open rooms of the current map used.
     */
    private void move() {
        System.out.println("[e] Exit map");

        Room currRoom = map.getHeroLocation();
        if (map.canMoveToAdjacentRoom(Direction.NORTH, currRoom)) System.out.println("[W] Move up");
        if (map.canMoveToAdjacentRoom(Direction.SOUTH, currRoom)) System.out.println("[S] Move down");
        if (map.canMoveToAdjacentRoom(Direction.EAST, currRoom)) System.out.println("[D] Move right");
        if (map.canMoveToAdjacentRoom(Direction.WEST, currRoom)) System.out.println("[A] Move left");

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
        map.showMap();
        if (!finalBoss.isAlive()) {
            gameOver = true;
            return;
        }
        move();
    }

    /**
     * Moves the unit to the specified direction. If the room is not avaialble
     * it will alert the player and requests a new input for another room;
     * @param to
     */
    private void moveTo(Direction to) {
        Room rm = map.getHeroLocation();
        if (map.canMoveToAdjacentRoom(to, rm)) {
            map.setHeroLocation(rm.getAdjacentRoom(to));
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
        Room room = map.getHeroLocation();
        Unit enemy = room.getEnemy();
        // Checks if the room has an enemy
        if (enemy == null) return;

        //Checks if the enemy is still alive
        if (enemy.isAlive()) {
            bm.setEnemy(enemy);

            //Will check who wins the battle. returns 1 if the player wins and
            // returns zero if otherwise.
            if (bm.toBattle() > 0) {
                System.out.println("You have slain " + GREEN + enemy.getName() + BLACK);
                checkBossIsAlive();
            } else {
                System.out.println(RED + enemy.getName() + " has slain you" + BLACK);
                if (fightAgain()) {
                    checkRoomForEnemy();
                } else {
                    map.setHeroLocation(previousRoom);
                }
            }
        }
    }

    public boolean gameIsOver() {
        return gameOver;
    }

    /**
     * Checks if the boss of the current progress is still alive
     */
    private boolean progressBossIsAlive() {
        Room bossRoom = map.getBossRoom();
        return bossRoom.getEnemy().isAlive();
    }

    /**
     * Increments the progress if the boss of the current progress is slain.
     */
    private void raiseProgress() {
        // Will end the game if the final boss is slain
        if (progress == gameMapProgress.size() - 1) {
            Broadcaster.relayMissionAccomplished();
            gameOver = true;
            return;
        }

        // changes the location of the hero
        if (progress == 2) map.setHeroLocation(secondLocation);
        map.setGameMap(gameMapProgress.getOpenedRooms(++progress));
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
        Room room = map.getBossRoom();
        Unit enemyBoss = room.getEnemy();

        if (!enemyBoss.isAlive()) {
            raiseProgress();
        }
    }

    /**
     * Shows the characters stats, damage and health
     */
    private void showCharacter() {
        System.out.println("==== "+ hero.getName() +" ====");
        System.out.println("Health: " + hero.getHealth().getMaxHealth());
        System.out.println("Damage: " + hero.getMinDamage() + "-" + hero.getMaxHealth());
        System.out.println("Skill: " + hero.getSkill().getName());
        System.out.println();
        System.out.println("\t~~~Stats~~~");
        Stats stats = hero.getUnitStats();
        for (StatType statType: StatType.values()) {
            System.out.println(statType.toString() + ": " + stats.getStatValue(statType));
        }
        System.out.printf("\n");
    }
}
