package game.engine;

import components.geography.GameMap;
import components.geography.Point;
import components.geography.Room;
import components.item.EquippableItem;
import components.item.Item;
import components.skill.list.*;
import components.unit.SkilledUnit;
import components.unit.Unit;
import components.unit.UnskilledUnit;
import dialogue.Dialogue;
import misc.Direction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GameInitializer implements Initializer {
    private Map<String, Unit> units = new HashMap<>();
    private Map<String, Item> items = new HashMap<>();
    private GameMap mapOne;
    private GameMap mapTwo;
    private SkilledUnit hero;
    private Unit finalBoss;
    private Progress mapOneProgress = new Progress();
    private Progress mapTwoProgress = new Progress();
    private Dialogue dialogue = new Dialogue();

    public Unit getFinalBoss() {
        return finalBoss;
    }

    public SkilledUnit getHero() {
        return hero;
    }

    public Dialogue getDialogue() {
        return dialogue;
    }

    public Progress getMapOneProgress() {
        return mapOneProgress;
    }

    public Progress getMapTwoProgress() {
        return mapTwoProgress;
    }

    public GameMap getGameMapOne() {
        return mapOne;
    }

    public GameMap getGameMapTwo() {
        return mapTwo;
    }

    public Map<String, Item> getItems() {
        return items;
    }

    public void initialize() {
        setupUnits();
        setupItems();
        setupRooms();
        setupDialogue();
    }

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

        // Setting the adjacent rooms for each Rooms
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

        //Setting up the two maps
        Room[] firstProgress_m1 = new Room[] {hallwayOne, hallwayTwo, livingRoom, servantQuarters};
        Room[] secondProgress_m1 = new Room[] {diningHall, kitchen};
        Room[] thirdProgress_m1 = new Room[] {hallwayThree, masterBedroom};
        mapOneProgress.setRoomsOpened(firstProgress_m1);
        mapOneProgress.setRoomsOpened(secondProgress_m1);
        mapOneProgress.setRoomsOpened(thirdProgress_m1);

        Room[] firstProgress_m2 = new Room[] {masterBedroom_2, hallwayThree_2, hallwayTwo_2, diningHall_2, kitchen_2};
        Room[] secondProgress_m2 = new Room[] {livingRoom_2, servantQuarters_2};
        Room[] thirdProgress_m2 = new Room[] {hallwayOne_2};
        mapTwoProgress.setRoomsOpened(firstProgress_m2);
        mapTwoProgress.setRoomsOpened(secondProgress_m2);
        mapTwoProgress.setRoomsOpened(thirdProgress_m2);

        ArrayList<Room> openRooms = new ArrayList<>();
        ArrayList<Room> rooms = new ArrayList<>();
        Room heroLocation;

        rooms.add(hallwayOne);
        rooms.add(hallwayTwo);
        rooms.add(hallwayThree);
        rooms.add(livingRoom);
        rooms.add(servantQuarters);
        rooms.add(diningHall);
        rooms.add(kitchen);
        rooms.add(masterBedroom);

        openRooms.add(hallwayOne);
        openRooms.add(hallwayTwo);
        openRooms.add(livingRoom);
        openRooms.add(servantQuarters);

        heroLocation = hallwayOne;                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                            ;
        mapOne = new GameMap(rooms, mapOneProgress.getOpenedRooms(0), heroLocation);

        ArrayList<Room> openRooms_2 = new ArrayList<>();
        Room heroLocation_2;

        openRooms_2.add(masterBedroom_2);
        openRooms_2.add(hallwayThree_2);
        openRooms_2.add(hallwayTwo_2);
        openRooms_2.add(livingRoom_2);
        openRooms_2.add(servantQuarters_2);

        heroLocation_2 = masterBedroom_2;
        mapTwo = new GameMap((ArrayList<Room>) rooms.clone(), mapTwoProgress.getOpenedRooms(0), heroLocation_2);
    }

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

        // == Construction of units ==

        //Hero of the game
        SkilledUnit alucard = new SkilledUnit.Builder()
                .name("Alucard")
                .health(1000)
                .damage(1000)
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

    @Override
    public void setupItems() {
        Item commonSword = new EquippableItem.Builder("Common Sword")
                .damage(10)
                .build();
        Item rareSword = new EquippableItem.Builder("Rare Sword")
                .damage(25)
                .health(50)
                .criticalChance(5)
                .build();
        Item rapier = new EquippableItem.Builder("Rapier")
                .damage(150)
                .criticalChance(25)
                .lifesteal(25)
                .evasion(10)
                .build();
        Item chainMail = new EquippableItem.Builder("Chain Mail")
                .health(30)
                .build();
        Item breastPlate = new EquippableItem.Builder("Breast Plate")
                .health(80)
                .damage(15)
                .evasion(5)
                .lifesteal(5)
                .build();
        Item kevlar = new EquippableItem.Builder("Kevlar")
                .health(200)
                .evasion(15)
                .criticalChance(10)
                .lifesteal(5)
                .damage(50)
                .build();
        Item redMoon = new EquippableItem.Builder("Red Moon")
                .health(40)
                .damage(5)
                .evasion(5)
                .criticalChance(5)
                .lifesteal(10)
                .build();
        Item talisman = new EquippableItem.Builder("Vampire's Talisman")
                .health(10)
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
    }

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
