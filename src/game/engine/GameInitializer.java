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
import misc.EquipmentType;
import misc.SkillList;
import misc.StatType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GameInitializer implements Initializer{
    private ArrayList<Room>       openRooms = new ArrayList<>();
    private ArrayList<Room>       rooms  = new ArrayList<>();
    private ArrayList<Unit>       units  = new ArrayList<>();
    private Map<SkillList, Skill> skills = new HashMap();
    private ArrayList<Item>       items  = new ArrayList<>();
    private GameMap               map;
    private Point heroLocation;

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

    @Override
    public void setupRooms() {
        Room hallwayOne      = new Room("Hallway One", new Point(0, -1));
        Room hallwayTwo      = new Room("Hallway Two", new Point(0, 0));
        Room hallwayThree    = new Room("Hallway Three", new Point(0, 1));
        Room livingRoom      = new Room("Living Room", new Point(-1, 0));
        Room servantQuarters = new Room("Servant Quarters", new Point(-2, 0));
        Room diningHall      = new Room("Dining Hall", new Point(1,0));
        Room kitchen         = new Room("Kitchen",new Point(2,0));
        Room masterBedroom   = new Room("Master's Bedroom", new Point(0,2));

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

        heroLocation = hallwayOne.getPoint();
        map          = new GameMap(openRooms, heroLocation);
    }

    @Override
    public void setupUnits() {
        this.setupSkills();
        SkilledUnit alucard   = new SkilledUnit.Builder()
                .damage(10).health(100).name("Alucard").stat(StatType.LIFESTEAL, 7)
                .setSkill(skills.get(SkillList.SOUL_STEAL)).build();

        SkilledUnit dracula   = new SkilledUnit.Builder()
                .damage(65).health(700).name("Vlad the Impaler").stat(StatType.EVASION, 25)
                .stat(StatType.CRITICAL_CHANCE, 25).stat(StatType.LIFESTEAL, 25)
                .setSkill(skills.get(SkillList.CHAOS_STRIKE)).build();

        SkilledUnit warlock   = new SkilledUnit.Builder()
                .damage(25).health(200).name("Warlock")
                .stat(StatType.EVASION, 10).stat(StatType.CRITICAL_CHANCE, 10)
                .stat(StatType.LIFESTEAL, 25).setSkill(skills.get(SkillList.HEAL)).build();

        SkilledUnit werewolf  = new SkilledUnit.Builder()
                .damage(40).health(250).name("Werewolf").stat(StatType.EVASION, 10)
                .stat(StatType.CRITICAL_CHANCE, 10).stat(StatType.LIFESTEAL, 25)
                .setSkill(skills.get(SkillList.FIRE_BOLT)).build();

        SkilledUnit minotaur  = new SkilledUnit.Builder()
                .damage(60).health(350).name("Minotaur").stat(StatType.EVASION, 5)
                .stat(StatType.CRITICAL_CHANCE, 25).stat(StatType.LIFESTEAL, 5)
                .setSkill(skills.get(SkillList.GREATER_BUFF)).build();

        SkilledUnit medusa    = new SkilledUnit.Builder()
                .damage(15).health(110).name("Medusa").stat(StatType.EVASION, 5)
                .stat(StatType.CRITICAL_CHANCE, 10)
                .setSkill(skills.get(SkillList.LESSER_HEAL)).build();

        SkilledUnit casper    = new SkilledUnit.Builder()
                .damage(20).health(170).name("Casper").stat(StatType.EVASION, 10)
                .stat(StatType.CRITICAL_CHANCE, 5)
                .setSkill(skills.get(SkillList.MINOR_BUFF)).build();

        SkilledUnit lilith    = new SkilledUnit.Builder()
                .damage(30).health(210).name("Lilith").stat(StatType.CRITICAL_CHANCE, 10)
                .stat(StatType.LIFESTEAL, 10)
                .setSkill(skills.get(SkillList.LIGHTNING_BOLT)).build();

        SkilledUnit general   = new SkilledUnit.Builder()
                .damage(35).health(250).name("General Milling").stat(StatType.EVASION, 10)
                .stat(StatType.CRITICAL_CHANCE, 25).stat(StatType.LIFESTEAL, 10).build();

        SkilledUnit priestess = new SkilledUnit.Builder()
                .damage(40).health(400).name("Priestess").stat(StatType.EVASION, 25)
                .stat(StatType.CRITICAL_CHANCE, 10).stat(StatType.LIFESTEAL, 10).build();

        UnskilledUnit banshee = new UnskilledUnit.Builder()
                .damage(8).health(80).name("Banshee").stat(StatType.EVASION, 5).build();
        UnskilledUnit imp     = new UnskilledUnit.Builder()
                .damage(12).health(100).name("Imp").stat(StatType.CRITICAL_CHANCE, 5).build();
        UnskilledUnit vampire = new UnskilledUnit.Builder()
                .damage(15).health(120).name("Vampire").stat(StatType.LIFESTEAL, 5).build();

    }

    @Override
    public void setupItems() {
        Item commonSword = new EquippableItem.Builder("Common Sword", EquipmentType.WEAPON)
                .damage(10).build();
        Item rareSword   = new EquippableItem.Builder("Rare Sword", EquipmentType.WEAPON)
                .damage(25).health(50).stat(StatType.CRITICAL_CHANCE, 5).build();
        Item rapier      = new EquippableItem.Builder("Rapier", EquipmentType.WEAPON)
                .damage(150).stat(StatType.CRITICAL_CHANCE, 25).stat(StatType.LIFESTEAL, 25)
                .stat(StatType.EVASION, 10).build();
        Item chainMail   = new EquippableItem.Builder("Chain Mail", EquipmentType.ARMOR)
                .health(30).build();
        Item breastPlate = new EquippableItem.Builder("Breast Plate", EquipmentType.ARMOR)
                .health(80).damage(15).stat(StatType.EVASION, 5).stat(StatType.LIFESTEAL, 5).build();
        Item kevlar      = new EquippableItem.Builder("Kevlar", EquipmentType.ARMOR)
                .health(200).stat(StatType.EVASION,15).stat(StatType.CRITICAL_CHANCE, 10)
                .stat(StatType.LIFESTEAL,5).damage(50).build();
        Item redMoon     = new EquippableItem.Builder("Red Moon", EquipmentType.AMULET)
                .health(40).damage(5).stat(StatType.EVASION, 5).stat(StatType.CRITICAL_CHANCE,5)
                .stat(StatType.LIFESTEAL, 10).build();
        Item Talisman    = new EquippableItem.Builder("Vampire's Talisman", EquipmentType.AMULET)
                .health(10).stat(StatType.LIFESTEAL, 25).build();

        items.add(commonSword);
        items.add(rareSword);
        items.add(rapier);
        items.add(chainMail);
        items.add(breastPlate);
        items.add(kevlar);
        items.add(redMoon);
        items.add(Talisman);
    }

    @Override
    public void setupSkills() {
        HealSkill lesserHeal      = new HealSkill("Lesser Heal");
        HealSkill heal            = new HealSkill("Heal");
        DamageSkill lightningBolt = new DamageSkill("Lightning Bolt", 15);
        DamageSkill fireBolt      = new DamageSkill("Fire Bolt", 30);
        SoulSteal soulSteal       = new SoulSteal();
        ChaosStrike chaosStrike   = new ChaosStrike();
        StatBoostSkill minorBuff  = new StatBoostSkill.Builder("Minor Buff")
                .criticalChance(5).evasion(5).lifesteal(5).duration(3).build();
        StatBoostSkill greaterBuff = new StatBoostSkill.Builder("Improved Buff")
                .criticalChance(10).evasion(10).lifesteal(10).duration(5).build();

        skills.put(SkillList.LESSER_HEAL, lesserHeal);
        skills.put(SkillList.HEAL, heal);
        skills.put(SkillList.LIGHTNING_BOLT, lightningBolt);
        skills.put(SkillList.FIRE_BOLT, fireBolt);
        skills.put(SkillList.SOUL_STEAL, soulSteal);
        skills.put(SkillList.CHAOS_STRIKE, chaosStrike);
        skills.put(SkillList.MINOR_BUFF, minorBuff);
        skills.put(SkillList.GREATER_BUFF, greaterBuff);
    }
}
