package game.engine;

import components.Health;
import components.Stats;
import components.geography.GameMap;
import components.geography.Room;
import components.item.EquippableItem;
import components.item.Inventory;
import components.item.Item;
import components.item.UnitEquipment;
import components.unit.SkilledUnit;
import components.unit.Unit;
import dialogue.GameNarrative;
import dialogue.Narrative;
import misc.*;
import util.StatHelper;

import java.util.List;
import java.util.Scanner;

import static misc.TextColor.*;
import static util.Sleep.sleep;

public class GameManager implements GameCycle, GameOver {

    private static final Scanner scanner = new Scanner(System.in);

    private boolean gameOver;
    private int progress;
    private BattleManager bm;
    private GameMap map;
    private GameMapProgress gameMapProgress;
    private GameNarrative gameNarrative;
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
        finalBoss = gi.getFinalBoss();
        gameInventory = gi.getItems();
        gameMapProgress = gi.getGameMapProgress();
        gameOver = false;
        gameNarrative = gi.getGameNarrative();
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
        checkRoomVariables();
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

        System.out.println();
        System.out.println(ANSI_BLACK + "[I] Inventory");
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
            map.showMap();
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
        checkForItem();
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
        if (progress == gameMapProgress.size() - 1) {
            Broadcaster.relayMissionAccomplished();
            gameOver = true;
            return;
        }

        // changes the location of the hero
        if (progress == 2) {
            getNarrative(0);
            map.setHeroLocation(secondLocation);
        }
        map.setGameMap(gameMapProgress.getOpenedRooms(++progress));
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

    private void showInventory() throws NullPointerException {
        int index = 1;
        System.out.println();
        System.out.println(ANSI_BLACK + "===========================");
        System.out.println(ANSI_PURPLE + "Inventory\n");
        List<EquippableItem> items = heroInventory.getInventory();
        for (EquippableItem i : items) {
            System.out.println(index++ + ". " + i.getName());
        }
        System.out.println(ANSI_BLACK);

    }

    private void showEquipment() {

        System.out.println(ANSI_BLACK + "===========================" + ANSI_CYAN);
        System.out.println("Equipment\n");
        for (EquipmentType equipmentType: EquipmentType.values()) {
            EquippableItem item = equipment.getItem(equipmentType);
            try {
                System.out.println(equipmentType.toString() + ": " + item.getName());
            } catch (NullPointerException e) {
                System.out.println(equipmentType.toString() + ": ");
            }
        }
        System.out.println(ANSI_BLACK);
    }

    private void checkForItem() {
        Room room = map.getHeroLocation();
        EquippableItem item = room.getItem();

        if (heroInventory.contains(item)) return;

        if (gameInventory.contains(item)) {
            heroInventory.addItem(item);

            sleep(1);
            System.out.println(ANSI_GREEN + "\nYou found an item" + ANSI_BLACK);
            sleep(1);
            System.out.println(item.getName() + " is added in your inventory");
            sleep(3);
            System.out.println();
        }
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
            sleep(1);
            System.out.println(ANSI_RED + "There is an enemy in the room prepare for battle" + ANSI_BLACK);
            sleep(1);

            //Will check who wins the battle. returns 1 if the player wins and
            // returns zero if otherwise.
            if (bm.toBattle() > 0) {
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

    private void openInventory() {
        showInventory();
        showEquipment();
        System.out.println();
        System.out.println("[E] Exit");
        System.out.println("[I] Inspect Item");
        System.out.println("[U] Equip Item");

        Scanner sc = new Scanner(System.in);

        switch (sc.nextLine().toLowerCase()) {
            case "e":
                return;
            case "i":
                System.out.println();
                System.out.println(ANSI_BLUE+ "Item no.:" +ANSI_BLACK);
                inspectItem(sc.nextInt());
                break;
            case "u":
                System.out.println();
                System.out.println(ANSI_BLUE + "Item no.:" + ANSI_BLACK);
                useItem(sc.nextInt());
                break;
            default:
                System.out.println(ANSI_RED + "Invalid input, try again." + ANSI_BLACK);
                System.out.println();
                openInventory();
        }
    }

    private void inspectItem(int index) {
        if (heroInventory.getItem(index) == null) {
            System.out.println(ANSI_RED + "Sorry wrong input or item does not exist" + ANSI_BLACK);
            openInventory();
        }
        EquippableItem item = heroInventory.getItem(index);
        System.out.println();
        System.out.println("Name: " + item.getName());
        System.out.println("Ty[e: " + item.getEquipmentType());
        System.out.println(ANSI_PURPLE + "Item Boost");
        if (item.getDamage() > 0 ) System.out.println("Damage: +" + item.getDamage());
        if (item.getHealthBoost() > 0 ) System.out.println("Health Boost: +" + item.getHealthBoost());

        Stats s = item.getItemStats();
        for (StatType statType: StatType.values()) {
            if (s.getStatValue(statType) > 0 ) System.out.println(statType.toString() + ": +" + s.getStatValue(statType));
        }
        System.out.println(ANSI_BLACK);
        showEquipment();
    }

    private void useItem(int index){
        if (heroInventory.getItem(index) == null) {
            System.out.println("Sorry wrong input or item does not exist");
            openInventory();
        }


        sleep(1);
        EquippableItem item = heroInventory.getItem(index);
        EquipmentType type = item.getEquipmentType();
        Stats heroStats = hero.getUnitStats();
        Stats itemStats = item.getItemStats();
        Item prevItem = equipment.getItem(type);

        sleep(1);
        System.out.println("Equipping " + item.getName());
        sleep(1);

        try {
            StatHelper.decreaseStats(heroStats, prevItem.getItemStats());
            StatHelper.increaseStats(heroStats, itemStats);
        } catch (NullPointerException e) {
            StatHelper.increaseStats(heroStats, itemStats);
        } finally {
            equipment.equipItem(item);
        }

        Health h = hero.getHealth();
        if (item.getHealthBoost() > 0) {
            System.out.println(ANSI_GREEN + "Health: + " + item.getHealthBoost());
            h.setMaxHealth(h.getMaxHealth() + item.getHealthBoost());
        }

        if (item.getDamage() > 0) {
            hero.setDamage(hero.getDamage() + item.getDamage());
            System.out.println(ANSI_GREEN + "Damage: +" + item.getDamage());
        }
        sleep(2);
        showEquipment();
    }

    private void getNarrative(int index) {
        Room r = map.getHeroLocation();
        Narrative n = gameNarrative.getNarrative(r);

        try {
            if (n.isNarrated()) {
                return;
            }
        } catch (NullPointerException e) {
            return;
        }

        try {
            String[] strings = n.getStrings(index);
            for (String s : strings) {
                sleep(3);
                System.out.println(ANSI_BLUE + s);
            }
        } catch (NullPointerException e) {

        }
        System.out.println(ANSI_BLACK);
        sleep(1);

        if (index > 0 ) {
            n.setNarrated(true);
        }
    }
}
