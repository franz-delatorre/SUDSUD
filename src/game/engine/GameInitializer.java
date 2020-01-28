package game.engine;

import components.geography.GameMap;
import components.geography.Point;
import components.geography.Room;
import components.item.EquippableItem;
import components.item.Inventory;
import components.item.Item;
import components.skill.list.*;
import components.unit.SkilledUnit;
import components.unit.Unit;
import components.unit.UnskilledUnit;
import dialogue.Dialogue;
import misc.Direction;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.Adler32;

public class GameInitializer implements Initializer {
    private Dialogue dialogue = new Dialogue();
    private Map<String, Unit> units = new HashMap<>();
    private Map<String, Item> items = new HashMap<>();
    private GameMap gameMap;
    private GameMapProgress gameMapProgress = new GameMapProgress();
    private Inventory gameInventory = new Inventory();
    private Room secondLocation;
    private SkilledUnit hero;
    private Unit finalBoss;

    public Unit getFinalBoss() {
        return finalBoss;
    }

    public Room getSecondLocation() {
        return secondLocation;
    }

    public GameMap getGameMap() {
        return gameMap;
    }

    public GameMapProgress getGameMapProgress() {
        return gameMapProgress;
    }

    public SkilledUnit getHero() {
        return hero;
    }

    public Dialogue getDialogue() {
        return dialogue;
    }

    public Inventory getItems() {
        return gameInventory;
    }

    public void initialize() {
        setupUnits();
        setupItems();
        setupRooms();
        setupDialogue();
    }

    /**
     * Sets up the rooms in the game. There are two map for this game so each room will
     * have a clone instance of itself. The only difference they will have is/are the
     * enemy within the room. Will also set the game progress for each map. Game Progress
     * is used to open the other rooms in the map once the boss is slain.
     */
    @Override
    public void setupRooms() {
        // Rooms of the first map
        Room hallwayOne = new Room("Hallway One", new Point(0, -1));
        Room hallwayTwo = new Room("Hallway Two", new Point(0, 0));
        Room hallwayThree = new Room("Hallway Three", new Point(0, 1));
        Room livingRoom = new Room("Living Room", new Point(-1, 0));
        Room servantQuarters = new Room("Servant Quarters", new Point(-2, 0));
        Room diningHall = new Room("Dining Hall", new Point(1, 0));
        Room kitchen = new Room("Kitchen", new Point(2, 0));
        Room masterBedroom = new Room("Master's Bedroom", new Point(0, 2));

        // Rooms of the second map
        Room hallwayOne_2 = hallwayOne.clone();
        Room hallwayTwo_2 = hallwayTwo.clone();
        Room hallwayThree_2 = hallwayThree.clone();
        Room livingRoom_2 = livingRoom.clone();
        Room servantQuarters_2 = servantQuarters.clone();
        Room diningHall_2 = diningHall.clone();
        Room kitchen_2 = kitchen.clone();
        Room masterBedroom_2 = masterBedroom.clone();

        // Setting the items inside some rooms
        hallwayTwo.setItem(items.get("commonSword"));
        livingRoom.setItem(items.get("redMoon"));
        kitchen.setItem(items.get("chainMail"));
        masterBedroom.setItem(items.get("rareSword"));
        kitchen_2.setItem(items.get("kevlar"));
        livingRoom_2.setItem(items.get("talisman"));
        servantQuarters_2.setItem(items.get("rapier"));

        // Setting the enemies in each rooms
        livingRoom.setEnemy(units.get("banshee"));
        servantQuarters.setEnemy(units.get("medusa"));
        diningHall.setEnemy(units.get("imp"));
        kitchen.setEnemy(units.get("casper"));
        hallwayThree.setEnemy(units.get("vampire"));
        masterBedroom.setEnemy(units.get("lilith"));

        hallwayThree_2.setEnemy(units.get("warlock"));
        livingRoom_2.setEnemy(units.get("werewolf"));
        diningHall_2.setEnemy(units.get("minotaur"));
        kitchen_2.setEnemy(units.get("priestess"));
        servantQuarters_2.setEnemy(units.get("death"));
        hallwayOne_2.setEnemy(units.get("dracula"));

        // Setting the adjacent rooms for each rooms
        hallwayOne.setAdjacentRoom(Direction.NORTH, hallwayTwo);
        hallwayOne_2.setAdjacentRoom(Direction.NORTH, hallwayTwo_2);

        hallwayTwo.setAdjacentRoom(Direction.WEST, livingRoom);
        hallwayTwo.setAdjacentRoom(Direction.EAST, diningHall);
        hallwayTwo.setAdjacentRoom(Direction.NORTH, hallwayThree);

        hallwayTwo_2.setAdjacentRoom(Direction.WEST, livingRoom_2);
        hallwayTwo_2.setAdjacentRoom(Direction.EAST, diningHall_2);
        hallwayTwo_2.setAdjacentRoom(Direction.NORTH, hallwayThree_2);

        hallwayThree_2.setAdjacentRoom(Direction.NORTH, masterBedroom_2);
        hallwayThree.setAdjacentRoom(Direction.NORTH, masterBedroom);

        livingRoom.setAdjacentRoom(Direction.WEST, servantQuarters);
        livingRoom_2.setAdjacentRoom(Direction.WEST, servantQuarters_2);

        diningHall.setAdjacentRoom(Direction.EAST, kitchen);
        diningHall_2.setAdjacentRoom(Direction.EAST, kitchen_2);

        //Sets the opened rooms for each progress in the game.
        ArrayList<Room> progressOne = new ArrayList<>();
        progressOne.add(hallwayOne);
        progressOne.add(hallwayTwo);
        progressOne.add(livingRoom);
        progressOne.add(servantQuarters);

        ArrayList<Room> progressTwo = new ArrayList<>(progressOne);
        progressTwo.add(diningHall);
        progressTwo.add(kitchen);

        ArrayList<Room> progressThree = new ArrayList<>(progressTwo);
        progressThree.add(hallwayThree);
        progressThree.add(masterBedroom);

        ArrayList<Room> progressFour = new ArrayList<>();
        progressFour.add(masterBedroom_2);
        progressFour.add(hallwayThree_2);
        progressFour.add(hallwayTwo_2);
        progressFour.add(livingRoom_2);
        progressFour.add(servantQuarters_2);

        ArrayList<Room> progressFive = new ArrayList<>(progressFour);
        progressFive.add(diningHall_2);
        progressFive.add(kitchen_2);

        ArrayList<Room> progressSix = new ArrayList<>(progressFive);
        progressSix.add(hallwayOne_2);

        gameMapProgress.addRoomsOpened(progressOne);
        gameMapProgress.addRoomsOpened(progressTwo);
        gameMapProgress.addRoomsOpened(progressThree);
        gameMapProgress.addRoomsOpened(progressFour);
        gameMapProgress.addRoomsOpened(progressFive);
        gameMapProgress.addRoomsOpened(progressSix);

        gameMap = new GameMap(gameMapProgress.getOpenedRooms(0), hallwayOne);
        secondLocation = masterBedroom_2;
    }

    /**
     * Will setup the skills first then create the units and bind the skills for
     * each unit.
     */
    @Override
    public void setupUnits() {

        // == Construction of Skills ==
        HealSkill lesserHeal = new HealSkill("Lesser Heal");
        HealSkill heal = new HealSkill("Heal");
        DamageSkill lightningBolt = new DamageSkill("Lightning Bolt", 15);
        DamageSkill fireBolt = new DamageSkill("Fire Bolt", 30);
        SoulSteal soulSteal = new SoulSteal();
        ChaosStrike chaosStrike = new ChaosStrike();
        StatBoostSkill minorBuff = new StatBoostSkill.Builder("Minor Buff")
                .criticalChance(5)
                .evasion(5)
                .lifesteal(5)
                .duration(3)
                .build();
        StatBoostSkill greaterBuff = new StatBoostSkill.Builder("Improved Buff")
                .criticalChance(10)
                .evasion(10)
                .lifesteal(10)
                .duration(5)
                .build();
        heal.setHeal(60);
        lesserHeal.setHeal(30);

        // == Construction of units ==

        //Hero of the game
        SkilledUnit alucard = new SkilledUnit.Builder()
                .name("Alucard")
                .health(1000)
                .damage(10)
                .lifesteal(0)
                .criticalChance(5)
                .evasion(0)
                .setSkill(soulSteal)
                .build();
        hero = alucard;

        //All other units
        SkilledUnit dracula = new SkilledUnit.Builder()
                .damage(65).health(700)
                .name("Vlad the Impaler")
                .evasion(25)
                .criticalChance(25)
                .lifesteal(25)
                .setSkill(chaosStrike)
                .build();
        finalBoss = dracula;

        SkilledUnit warlock = new SkilledUnit.Builder()
                .damage(25)
                .health(200)
                .name("Warlock")
                .evasion(10)
                .criticalChance(10)
                .lifesteal(25)
                .setSkill(heal)
                .build();
        SkilledUnit werewolf = new SkilledUnit.Builder()
                .damage(40)
                .health(250)
                .name("Werewolf")
                .evasion(10)
                .criticalChance(10)
                .lifesteal(25)
                .setSkill(fireBolt)
                .build();
        SkilledUnit minotaur = new SkilledUnit.Builder()
                .damage(60)
                .health(350)
                .name("Minotaur")
                .evasion(5)
                .criticalChance(25)
                .lifesteal(5)
                .setSkill(greaterBuff)
                .build();
        SkilledUnit medusa = new SkilledUnit.Builder()
                .damage(15)
                .health(110)
                .name("Medusa")
                .evasion(5)
                .criticalChance(10)
                .setSkill(lesserHeal)
                .build();
        SkilledUnit casper = new SkilledUnit.Builder()
                .damage(20)
                .health(170)
                .name("Casper")
                .evasion(10)
                .criticalChance(5)
                .setSkill(minorBuff)
                .build();
        SkilledUnit lilith = new SkilledUnit.Builder()
                .damage(30)
                .health(210)
                .name("Lilith")
                .criticalChance(10)
                .lifesteal(10)
                .setSkill(lightningBolt)
                .build();
        SkilledUnit general = new SkilledUnit.Builder()
                .damage(35)
                .health(250)
                .name("General Milling")
                .evasion(10)
                .criticalChance(25)
                .lifesteal(10)
                .setSkill(greaterBuff)
                .build();
        SkilledUnit priestess = new SkilledUnit.Builder()
                .damage(40)
                .health(400)
                .name("Priestess")
                .evasion(25)
                .criticalChance(10)
                .lifesteal(10)
                .setSkill(chaosStrike)
                .build();
        SkilledUnit death = new SkilledUnit.Builder()
                .damage(50)
                .health(440)
                .name("Jack D' Reaper")
                .evasion(10)
                .criticalChance(25)
                .lifesteal(10)
                .setSkill(chaosStrike)
                .build();
        UnskilledUnit banshee = new UnskilledUnit.Builder()
                .damage(8)
                .health(80)
                .name("Banshee")
                .evasion(5)
                .build();
        UnskilledUnit imp = new UnskilledUnit.Builder()
                .damage(12)
                .health(100)
                .name("Imp")
                .criticalChance(5)
                .build();
        UnskilledUnit vampire = new UnskilledUnit.Builder()
                .damage(15)
                .health(120)
                .name("Vampire")
                .lifesteal(10)
                .build();

        // Adding the units in a hashmap
        units.put("dracula", dracula);
        units.put("warlock", warlock);
        units.put("werewolf", werewolf);
        units.put("minotaur", minotaur);
        units.put("medusa", medusa);
        units.put("casper", casper);
        units.put("lilith", lilith);
        units.put("general", general);
        units.put("priestess", priestess);
        units.put("banshee", banshee);
        units.put("imp", imp);
        units.put("vampire", vampire);
        units.put("death", death);
    }

    //Sets up the items in the game then saves it in an inventory named gameInventory
    @Override
    public void setupItems() {
        Item commonSword = new EquippableItem.Builder()
                .name("Common Sword")
                .damage(10)
                .build();
        Item rareSword = new EquippableItem.Builder()
                .name("Rare Sword")
                .damage(25)
                .healthBoost(50)
                .criticalChance(5)
                .build();
        Item rapier = new EquippableItem.Builder()
                .name("Rapier")
                .damage(150)
                .criticalChance(25)
                .lifesteal(25)
                .evasion(10)
                .build();
        Item chainMail = new EquippableItem.Builder()
                .name("Chain Mail")
                .healthBoost(30)
                .build();
        Item breastPlate = new EquippableItem.Builder()
                .name("Breast Plate")
                .healthBoost(80)
                .damage(15)
                .evasion(5)
                .lifesteal(5)
                .build();
        Item kevlar = new EquippableItem.Builder()
                .name("Kevlar")
                .healthBoost(200)
                .evasion(15)
                .criticalChance(10)
                .lifesteal(5)
                .damage(50)
                .build();
        Item redMoon = new EquippableItem.Builder()
                .name("Red Moon")
                .healthBoost(40)
                .damage(5)
                .evasion(5)
                .criticalChance(5)
                .lifesteal(10)
                .build();
        Item talisman = new EquippableItem.Builder()
                .name("Vampire's Talisman")
                .healthBoost(10)
                .lifesteal(25)
                .build();

        items.put("commonSword", commonSword);
        items.put("rareSword", rareSword);
        items.put("rapier", rapier);
        items.put("chainMail", chainMail);
        items.put("breastPlate", breastPlate);
        items.put("kevlar", kevlar);
        items.put("redMoon", redMoon);
        items.put("talisman", talisman);

        gameInventory.addItem(commonSword);
        gameInventory.addItem(rareSword);
        gameInventory.addItem(rapier);
        gameInventory.addItem(chainMail);
        gameInventory.addItem(breastPlate);
        gameInventory.addItem(kevlar);
        gameInventory.addItem(redMoon);
        gameInventory.addItem(talisman);
    }

    /**
     * Sets up the game dialogue for each progress.
     */
    @Override
    public void setupDialogue() {
        // Act 0
        String[] actZero = new String[] {
                "Welcome to Castlevania. Let's inspect the map to know where you are at.",
                "Please press m."
        };

        String[] actOne = new String[] {
                "Look there's an item on the floor. Let's pick it up.",
                "You found: Common Sword"
        };

        String[] actTwo = new String[] {
                "Prepare for battle, there's an enemy up ahead.",
                "Ganbatte!"
        };

        dialogue.addDialogue(actZero);
        dialogue.addDialogue(actOne);
        dialogue.addDialogue(actTwo);

    }
}
