package game.engine;

import components.Stats;
import components.geography.GameMap;
import components.geography.Room;
import components.item.EquippableItem;
import components.item.Inventory;
import components.item.Item;
import components.item.UnitEquipment;
import components.unit.SkilledUnit;
import components.unit.Unit;
import dialogue.Dialogue;
import misc.*;
import util.Sleep;
import util.StatHelper;

import java.util.Map;
import java.util.Scanner;
import java.util.logging.Logger;

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
    private Inventory gameInventory;
    private Room previousRoom;
    private Room secondLocation;
    private SkilledUnit hero;
    private Unit finalBoss;
    private UnitEquipment equipment;

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
        equipment = new UnitEquipment();
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
                openInventory();
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

            checkRoomVariables();
            map.showMap();
            if (!finalBoss.isAlive()) {
                gameOver = true;
                return;
            }
            move();
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
     * will be moved back to the last room he went to. Also checks the room if there
     * is an item to be picked up when: the enemy is slain, player is in the room.
     */
    private void checkRoomVariables() {
        checkForEnemy();
        checkForItem();
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
        System.out.println("Damage: " + hero.getMinDamage() + "-" + hero.getDamage());
        System.out.println("Skill: " + hero.getSkill().getName());
        System.out.println();
        System.out.println("\t~~~Stats~~~");
        Stats stats = hero.getUnitStats();
        for (StatType statType: StatType.values()) {
            System.out.println(statType.toString() + ": " + stats.getStatValue(statType));
        }
        System.out.printf("\n");
    }

    private void showInventory() {
        int index = 1;
        System.out.println("Inventory");
        for (Item item: heroInventory.getInventory()) {
            System.out.println(index + ".) " + heroInventory.getItem(index).getName());
        }

        System.out.println("===========================\n");
        for (EquipmentType equipmentType: EquipmentType.values()) {
            Item item = equipment.getItem(equipmentType);
            System.out.println(equipmentType.toString() + ": ");
            if (item != null) {
                System.out.printf(item.getName());
            } else {

            }
        }
    }

    private void checkForItem() {
        Room room = map.getHeroLocation();
        Item item = room.getItem();

        if (heroInventory.contains(item)) return;

        if (gameInventory.contains(item)) {
            heroInventory.addItem(item);
        }

        Sleep.sleep(1);
        System.out.println("\nYou Found an item");
        Sleep.sleep(1);
        System.out.println(item.getName() + " is added in your inventory");
        Sleep.sleep(1);
        System.out.println();
    }

    private void checkForEnemy() {
        Room room = map.getHeroLocation();
        Unit enemy = room.getEnemy();
        // Checks if the room has an enemy
        if (enemy == null) {
            checkForItem();
            return;
        }

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
                    checkRoomVariables();
                } else {
                    map.setHeroLocation(previousRoom);
                }
            }
        }
    }

    private void openInventory() {
        showInventory();
        System.out.println("[e] Exit");
        System.out.println("[i] Inspect Item");
        System.out.println("[u] Equip Item");

        Scanner sc = new Scanner(System.in);

        switch (sc.nextLine().toLowerCase()) {
            case "e":
                return;
            case "i":
                System.out.println("Item no.:");
                inspectItem(sc.nextInt());
                break;
            case "u":
                System.out.println("Item no.:");
                useItem(sc.nextInt());
                break;
            default:
                System.out.println("Invalid input, try again.");
                openInventory();
        }
    }

    private void inspectItem(int index) {
        Item item = heroInventory.getItem(index);
        System.out.println(item.getDescription());
        System.out.println("Use Item?");
        System.out.println("[y] Yes");
        System.out.println("[e] Back to Inventory");

        Scanner sc = new Scanner(System.in);
        String opt = sc.nextLine().toLowerCase();

        switch (opt) {
            case "y":
                System.out.println("FGJKSAHFKJAHSFKHASJKFHASJKFKJASF");
                useItem(index);
                showInventory();
                break;
            default:
                inspectItem(index);
        }
    }

    private void useItem(int index) {

        Item item = heroInventory.getItem(index);
        EquipmentType type = ((EquippableItem) item).getEquipmentType();
        Stats heroStats = hero.getUnitStats();
        Stats itemStats = item.getItemStats();

        if (equipment.getItem(type) == null) {
            System.out.println("ajklskflkasjfj");
            StatHelper.increaseStats(heroStats, itemStats);
        } else {
            Item prevItem = equipment.getItem(type);
            StatHelper.decreaseStats(heroStats, prevItem.getItemStats());
            StatHelper.increaseStats(heroStats, itemStats);
        }
        EquippableItem it = (EquippableItem) item;
        equipment.equipItem(it);
    }

    private void equipItem(EquippableItem item) {
        Item i = equipment.getItem(item.getEquipmentType());
        Stats unitStats = hero.getUnitStats();
        Stats itemStats = item.getItemStats();
        if (i == null) {
            StatHelper.increaseStats(unitStats, itemStats);
            equipment.equipItem(item);
            return;
        }

        StatHelper.decreaseStats(unitStats, itemStats);
        StatHelper.increaseStats(unitStats, i.getItemStats());
        equipment.equipItem(item);
    }
}
