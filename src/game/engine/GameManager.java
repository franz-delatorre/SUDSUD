package game.engine;

import components.Stats;
import components.geography.GameMap;
import components.geography.Room;
import components.item.EquippableItem;
import components.item.Inventory;
import components.unit.SkilledUnit;
import components.unit.Unit;
import dialogue.GameNarrative;
import dialogue.Narrative;
import misc.*;
import service.BattleService;
import service.InventoryService;

import java.util.Scanner;

import static misc.TextColor.*;
import static util.Sleep.sleep;

public class GameManager {

    private static final int MAP_CHANGE = 3;
    private static final Scanner scanner = new Scanner(System.in);

    private boolean gameOver;
    private int progress;
    private BattleService bs;
    private GameMap map;
    private GameMapProgress gameMapProgress;
    private GameNarrative gameNarrative;
    private InventoryService inventoryService;
    private Inventory heroInventory;
    private Room previousRoom;
    private Room secondLocation;
    private SkilledUnit hero;
    private Unit finalBoss;

    public GameManager() {
        GameInitializer gi = new GameInitializer();
        gi.initialize();

        hero = gi.getHero();
        bs = new BattleService(hero);
        finalBoss = gi.getFinalBoss();
        inventoryService = gi.getInventoryService();
        gameMapProgress = gi.getGameMapProgress();
        gameOver = false;
        gameNarrative = gi.getGameNarrative();
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
        checkRoomVariables();
        while (progressBossIsAlive()) {
            getUserAction();
        }
    }

    /**
     * Gets the user's action in the main action UI.
     */
    private void getUserAction() {
        if (gameOver) return;
        System.out.println();
        System.out.println(ANSI_BLACK + "[I] Inventory");
        System.out.println("[C] Character");
        System.out.println("[M] Map");
        System.out.println("[Q] Quit");
        String opt = scanner.nextLine().toLowerCase();

        switch (opt) {
            case "i":
                inventoryService.openInventoryMenu();
                break;
            case "m":
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
                System.out.println("Are you sure you want to quit and end the game? [Y] [N]");
                switch (scanner.nextLine().toLowerCase()) {
                    case "y":
                        Broadcaster.relayGameOver();
                        gameOver = true;
                        break;
                    case "n":
                        getUserAction();
                }
                break;
        }
    }

    /**
     * Will move the character around the map. Users cannot move to the adjacent room if
     * it is not listed in the open rooms of the current map used.
     */
    private void move() {
        map.showMap();
        System.out.println();
        System.out.println("[E] Exit map");

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
    }

    /**
     * Moves the unit to the specified direction. If the room is not avaialble
     * it will alert the player and requests a new input for another room;
     * @param to
     */
    private void moveTo(Direction to) {
        Room rm = map.getHeroLocation();
        if (map.canMoveToAdjacentRoom(to, rm)) {
            previousRoom = rm;
            map.setHeroLocation(rm.getAdjacentRoom(to));

            checkRoomVariables();
            if (!finalBoss.isAlive()) {
                gameOver = true;
                return;
            }
            move();
        }
        else {
            System.out.println(ANSI_RED + "Wrong input, try again" + ANSI_BLACK);
            move();
        }
    }

    /**
     * Checks if the room has an enemy. If the enemy is alive it will
     * simulate a battle between the hero and the enemy. If the player is defeated
     * he will choose to fight again or not. If the player will not fight on then he
     * will be moved back to the last room he went to. Also checks the room if there
     * is an item to be picked up when: the enemy is slain, player is in the room.
     */
    private void checkRoomVariables() {
        getNarrative(0);
        checkForEnemy();
        checkRoomItem();
        getNarrative(1);
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
        if (progress == gameMapProgress.roomsOpenedSize() - 1) {
            Broadcaster.relayMissionAccomplished();
            gameOver = true;
            return;
        }

        // changes the location of the hero
        map.setOpenRooms(gameMapProgress.getRoomsOpened(++progress));
        if (progress == MAP_CHANGE) {
            map.setHeroLocation(secondLocation);
        }
    }

    /**
     * Asks the user if they will fight again after losing to the enemy.
     * @return
     */
    private boolean fightAgain() {
        System.out.println(ANSI_RED + "You Lost" +ANSI_BLACK+ ", fight again? [Y] [N]");
        switch (scanner.nextLine().toLowerCase()) {
            case "y":
                return true;
            case "n":
                map.setHeroLocation(previousRoom);
                return false;
            default:
                System.out.println(ANSI_RED + "Wrong input, try again" + ANSI_BLACK);
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
        System.out.println();
        System.out.println("==== "+ hero.getName() +" ====");
        System.out.println("Health: " + hero.getHealth().getMaxHealth());
        System.out.println("Damage: " + hero.getMinDamage() + "-" + hero.getDamage());
        System.out.println("Skill: " + hero.getSkill().getName());
        System.out.println();
        System.out.println(ANSI_PURPLE + "\t~~~Stats~~~");
        Stats stats = hero.getUnitStats();
        for (StatType statType: StatType.values()) {
            System.out.println(statType.toString() + ": " + stats.getStatValue(statType));
        }
        System.out.printf("\n" + ANSI_BLACK);
    }

    private void checkRoomItem() {
        Room room = map.getHeroLocation();
        EquippableItem item = room.getItem();

        // Checks if the item exists in the game inventory
        if (!inventoryService.gameInventoryContains(item)) return;

        //Checks if the item is already in the hero's inventory
        if (inventoryService.heroInventoryContains(item)) return;
        inventoryService.addItemToHeroInventory(item);
    }

    /**
     * Checks for an enemy. A battle will ensue if an enemy exist and then enemy is Alive.
     */
    private void checkForEnemy() {
        Room room = map.getHeroLocation();
        Unit enemy = room.getEnemy();

        //Checks if the enemy is still alive
        if (enemy.isAlive()) {
            sleep(1);
            System.out.println(ANSI_RED + "There is an enemy in the room prepare for battle" + ANSI_BLACK);
            sleep(1);

            //Will check who wins the battle. returns 1 if the player wins and
            // returns zero if otherwise.
            if (bs.toBattle(enemy) > 0) {
                System.out.println("You have slain " + ANSI_GREEN + enemy.getName() + ANSI_BLACK);
                System.out.println();
                checkBossIsAlive();
            } else {
                System.out.println(ANSI_RED + enemy.getName() + " has slain you" + ANSI_BLACK);
                System.out.println();
                if (fightAgain()) {
                    checkRoomVariables();
                } else {
                    map.setHeroLocation(previousRoom);
                }
            }
        }
    }

    /**
     * Gets the narrative of the room. Then sets the isNarrated value to true.
     * @param index
     */
    private void getNarrative(int index) {
        Room r = map.getHeroLocation();
        Narrative n = gameNarrative.getNarrative(r);

        if (n.isNarrated()) {
            return;
        }

        String[] strings = n.getNarrative(index);
        for (String s : strings) {
//                sleep(3);
            System.out.println(ANSI_BLUE + s);
        }
        System.out.println(ANSI_BLACK);
//        sleep(1);

        if (index > 0 ) {
            n.setNarrated(true);
        }
    }
}
