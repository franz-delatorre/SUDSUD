package game.engine;

import components.geography.GameMap;
import components.geography.Point;
import components.geography.Room;
import components.item.EquippableItem;
import components.item.Inventory;
import components.skill.list.*;
import components.unit.SkilledUnit;
import components.unit.Unit;
import components.unit.UnskilledUnit;
import dialogue.GameNarrative;
import dialogue.Narrative;
import misc.Direction;
import misc.EquipmentType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GameInitializer implements Initializer {
    private Map<String, Unit> units = new HashMap<>();
    private Map<String, EquippableItem> items = new HashMap<>();
    private Map<String, Narrative> narrativeList = new HashMap<>();
    private GameMap gameMap;
    private GameMapProgress gameMapProgress = new GameMapProgress();
    private GameNarrative gameNarrative = new GameNarrative();
    private Inventory gameInventory = new Inventory();
    private Room secondLocation;
    private SkilledUnit hero;
    private Unit finalBoss;

    public GameNarrative getGameNarrative() {
        return gameNarrative;
    }

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

    public Inventory getItems() {
        return gameInventory;
    }

    public void initialize() {
        setupUnits();
        setupItems();
        setupDialogue();
        setupRooms();
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
        Room servantQuarters = new Room("Servant's Quarters", new Point(-2, 0));
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
        hallwayThree_2.setItem(items.get("talismanEvasion"));
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

        // Setting the gameNarrative
        gameNarrative.addNarrative(hallwayOne, narrativeList.get("hallwayOne"));
        gameNarrative.addNarrative(hallwayTwo, narrativeList.get("hallwayTwo"));
        gameNarrative.addNarrative(livingRoom, narrativeList.get("livingRoom"));
        gameNarrative.addNarrative(servantQuarters, narrativeList.get("servantsQuarters"));
        gameNarrative.addNarrative(masterBedroom, narrativeList.get("mastersBedroom"));
        gameNarrative.addNarrative(masterBedroom_2, narrativeList.get("mastersBedroom_2"));
        gameNarrative.addNarrative(hallwayOne_2, narrativeList.get("dracula"));

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
                .health(110)
                .damage(15)
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
                .evasion(5)
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
                .health(140)
                .name("Casper")
                .evasion(10)
                .criticalChance(5)
                .setSkill(minorBuff)
                .build();
        SkilledUnit lilith = new SkilledUnit.Builder()
                .damage(30)
                .health(170)
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
        EquippableItem commonSword = new EquippableItem.Builder()
                .name("Common Sword")
                .evasion(5)
                .damage(10)
                .equipmentType(EquipmentType.WEAPON)
                .build();
        EquippableItem rareSword = new EquippableItem.Builder()
                .name("Rare Sword")
                .damage(50)
                .healthBoost(50)
                .criticalChance(15)
                .evasion(5)
                .lifesteal(5)
                .equipmentType(EquipmentType.WEAPON)
                .build();
        EquippableItem rapier = new EquippableItem.Builder()
                .name("Rapier")
                .damage(150)
                .criticalChance(25)
                .lifesteal(25)
                .evasion(10)
                .equipmentType(EquipmentType.WEAPON)
                .build();
        EquippableItem chainMail = new EquippableItem.Builder()
                .name("Chain Mail")
                .healthBoost(30)
                .equipmentType(EquipmentType.ARMOR)
                .build();
        EquippableItem breastPlate = new EquippableItem.Builder()
                .name("Breast Plate")
                .healthBoost(80)
                .damage(15)
                .evasion(5)
                .lifesteal(5)
                .equipmentType(EquipmentType.ARMOR)
                .build();
        EquippableItem kevlar = new EquippableItem.Builder()
                .name("Kevlar")
                .healthBoost(250)
                .evasion(15)
                .criticalChance(10)
                .lifesteal(5)
                .damage(50)
                .equipmentType(EquipmentType.ARMOR)
                .build();
        EquippableItem talismanEvasion = new EquippableItem.Builder()
                .name("Talisman of Evasion")
                .evasion(40)
                .equipmentType(EquipmentType.AMULET)
                .build();
        EquippableItem redMoon = new EquippableItem.Builder()
                .name("Red Moon")
                .healthBoost(40)
                .damage(5)
                .evasion(5)
                .criticalChance(5)
                .lifesteal(10)
                .equipmentType(EquipmentType.AMULET)
                .build();
        EquippableItem talisman = new EquippableItem.Builder()
                .name("Vampire's Talisman")
                .healthBoost(10)
                .lifesteal(25)
                .equipmentType(EquipmentType.AMULET)
                .build();

        items.put("commonSword", commonSword);
        items.put("rareSword", rareSword);
        items.put("rapier", rapier);
        items.put("chainMail", chainMail);
        items.put("breastPlate", breastPlate);
        items.put("kevlar", kevlar);
        items.put("redMoon", redMoon);
        items.put("talisman", talisman);
        items.put("talismanEvasion", talismanEvasion);

        gameInventory.addItem(commonSword);
        gameInventory.addItem(rareSword);
        gameInventory.addItem(rapier);
        gameInventory.addItem(chainMail);
        gameInventory.addItem(breastPlate);
        gameInventory.addItem(kevlar);
        gameInventory.addItem(redMoon);
        gameInventory.addItem(talisman);
        gameInventory.addItem(talismanEvasion);
    }

    /**
     * Sets up the game dialogue for each progress.
     */
    @Override
    public void setupDialogue() {
        String[] nullNarrative = new String[] {};

        String[] introduction = new String[] {
                "Welcome to Castlevania.",
                "I am Elmo the NPC",
                "Are you here to slay Count Dracula?",
                "But before that you first must defeat his army of evil"
        };

        String[] instructions = new String[] {
                "To move around the map press [M]",
                "Then select the location you want to go. [W = up, A = left, S = down, D = right]"
        };

        String[] foundAnItem = new String[] {
                "Great! You found an item.",
                "Items found will go directly to you inventory. press [I] to open your inventory",
                "Don't forget to inspect your item [I] and equip it to help you on your quest [U]",
                "But first we must exit the map, press [E]"
        };

        String[] enemyFound = new String[] {
                "Watch out! There's a banshee waiting for you at the corner",
                "Prepare for battle buddy and best of luck."
        };

        String[] enemySlain = new String[] {
                "Good job buddy! you killed that banshee!",
                "Look she dropped an item! Its an amulet! You'll look good if you wear it."
        };

        String[] bossFound = new String[] {
                "To arms! beyond this door is Medusa!",
                "Defeat the level boss and you'll open new rooms in the map",
                "Goodluck!!!"
        };

        String[] firstBossKill = new String[] {
                "Great! You killed the boss Medusa!",
                "Hold on the ground is shaking!!! ",
                ".............",
                "It looks like the castle is changing or something",
                "Hmmmmmmm"
        };

        String[] tierOneBossKill = new String[] {
                "Whoooooa!! It's shaking again!! Hold tight!",
                "..................",
                ".................."
        };

        String[] castleChange = new String[] {
                "Whoaaaa! I think the castle changed again.",
                "This time we are upside down!!",
        };

        String[] draculaRoom = new String[] {
                "I could feel Dracula's presence beyond this room",
                "Prepare yourself for the greatest battle of your life"
        };

        Narrative hallwayOneNarrative = new Narrative();
        Narrative hallwayTwoNarrative = new Narrative();
        Narrative livingRoomNarrative = new Narrative();
        Narrative servantsNarrative = new Narrative();
        Narrative masterNarrative = new Narrative();
        Narrative masterNarrative_2 = new Narrative();
        Narrative hallwayOneNarrative_2 = new Narrative();

        hallwayOneNarrative.addNarrative(introduction);
        hallwayOneNarrative.addNarrative(instructions);

        hallwayTwoNarrative.addNarrative(nullNarrative);
        hallwayTwoNarrative.addNarrative(foundAnItem);

        livingRoomNarrative.addNarrative(enemyFound);
        livingRoomNarrative.addNarrative(enemySlain);

        servantsNarrative.addNarrative(bossFound);
        servantsNarrative.addNarrative(firstBossKill);

        masterNarrative.addNarrative(nullNarrative);
        masterNarrative.addNarrative(tierOneBossKill);

        masterNarrative_2.addNarrative(castleChange);

        hallwayOneNarrative_2.addNarrative(draculaRoom);
        hallwayOneNarrative_2.addNarrative(nullNarrative);

        narrativeList.put("hallwayOne", hallwayOneNarrative);
        narrativeList.put("hallwayTwo", hallwayTwoNarrative);
        narrativeList.put("livingRoom", hallwayTwoNarrative);
        narrativeList.put("servantsQuarters", servantsNarrative);
        narrativeList.put("mastersBedroom", masterNarrative);
        narrativeList.put("mastersBedroom_2", masterNarrative_2);
        narrativeList.put("dracula", hallwayOneNarrative_2);
    }
}
