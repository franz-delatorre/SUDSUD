package game.engine;

import components.geography.GameMap;
import components.geography.Point;
import components.geography.Room;
import components.item.EquippableItem;
import components.item.Item;
import components.skill.Skill;
import components.skill.list.*;
import components.unit.SkilledUnit;
import components.unit.Unit;
import components.unit.UnskilledUnit;
import misc.Direction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GameInitializer implements Initializer {
    private ArrayList<Room> openRooms = new ArrayList<>();
    private ArrayList<Room> rooms = new ArrayList<>();
    private ArrayList<Unit> units = new ArrayList<>();
    private Map<Skill, Skill> skills = new HashMap();
    private ArrayList<Item> items = new ArrayList<>();
    private GameMap map;
    private Room heroLocation;

    public GameMap getGameMap() {
        return map;
    }

    public ArrayList<Room> getRooms() {
        return rooms;
    }

    public ArrayList<Unit> getUnits() {
        return units;
    }

    public ArrayList<Item> getItems() {
        return items;
    }

    public void initialize() {
        setupUnits();
        setupItems();
        setupRooms();
    }

    @Override
    public void setupRooms() {
        Room hallwayOne = new Room("Hallway One", new Point(0, -1));
        Room hallwayTwo = new Room("Hallway Two", new Point(0, 0));
        Room hallwayThree = new Room("Hallway Three", new Point(0, 1));
        Room livingRoom = new Room("Living Room", new Point(-1, 0));
        Room servantQuarters = new Room("Servant Quarters", new Point(-2, 0));
        Room diningHall = new Room("Dining Hall", new Point(1, 0));
        Room kitchen = new Room("Kitchen", new Point(2, 0));
        Room masterBedroom = new Room("Master's Bedroom", new Point(0, 2));

        hallwayOne.setAdjacentRoom(Direction.NORTH, hallwayTwo);

        hallwayTwo.setAdjacentRoom(Direction.WEST, livingRoom);
        hallwayTwo.setAdjacentRoom(Direction.EAST, diningHall);
        hallwayTwo.setAdjacentRoom(Direction.NORTH, hallwayThree);

        hallwayThree.setAdjacentRoom(Direction.NORTH, masterBedroom);

        livingRoom.setAdjacentRoom(Direction.WEST, servantQuarters);

        diningHall.setAdjacentRoom(Direction.EAST, kitchen);

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

        heroLocation = hallwayOne                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                ;
        map = new GameMap(openRooms, heroLocation);
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
        SkilledUnit alucard = new SkilledUnit.Builder()
                .damage(10)
                .health(100)
                .name("Alucard")
                .lifesteal(7)
                .setSkill(soulSteal)
                .build();
        SkilledUnit dracula = new SkilledUnit.Builder()
                .damage(65).health(700)
                .name("Vlad the Impaler")
                .evasion(25)
                .criticalChance(25)
                .lifesteal(25)
                .setSkill(chaosStrike)
                .build();
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
                .damage(40).health(250)
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

        Item Talisman = new EquippableItem.Builder("Vampire's Talisman")
                .health(10)
                .lifesteal(25)
                .build();

        items.add(commonSword);
        items.add(rareSword);
        items.add(rapier);
        items.add(chainMail);
        items.add(breastPlate);
        items.add(kevlar);
        items.add(redMoon);
        items.add(Talisman);
    }

}
