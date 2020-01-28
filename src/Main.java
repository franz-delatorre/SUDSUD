import components.Stats;
import components.item.EquippableItem;
import components.item.Item;
import components.item.UnitEquipment;
import components.unit.SkilledUnit;
import components.unit.Unit;
import game.engine.GameEngine;
import misc.EquipmentType;
import misc.StatType;
import util.StatHelper;

import java.awt.*;

public class Main {
    public static void main(String[] args) {
//        GameEngine gameEngine = new GameEngine();
//        gameEngine.start();

        UnitEquipment equipment = new UnitEquipment();
        EquippableItem sword = new EquippableItem.Builder().name("SWORD").damage(100).evasion(10).criticalChance(5).build();
        EquippableItem armor = new EquippableItem.Builder().name("ARMOR").healthBoost(10).lifesteal(10).build();
        Unit hero = new SkilledUnit.Builder().name("Franz").damage(10).evasion(10).build();
        EquippableItem dagger = new EquippableItem.Builder().name("dagger").lifesteal(40).damage(2).build();

        Stats stats = hero.getUnitStats();

        System.out.println("damage" + hero.getDamage());
        System.out.println("evasion" + stats.getStatValue(StatType.EVASION));
        System.out.println("crit" + stats.getStatValue(StatType.CRITICAL_CHANCE));
        System.out.println("lifesteal" + stats.getStatValue(StatType.LIFESTEAL));

        equipItem(hero, sword, equipment);

        System.out.println("damage" + hero.getDamage());
        System.out.println("evasion" + stats.getStatValue(StatType.EVASION));
        System.out.println("crit" + stats.getStatValue(StatType.CRITICAL_CHANCE));
        System.out.println("lifesteal" + stats.getStatValue(StatType.LIFESTEAL));

        equipItem(hero, dagger, equipment);

        System.out.println("damage" + hero.getDamage());
        System.out.println("evasion" + stats.getStatValue(StatType.EVASION));
        System.out.println("crit" + stats.getStatValue(StatType.CRITICAL_CHANCE));
        System.out.println("lifesteal" + stats.getStatValue(StatType.LIFESTEAL));
    }

    public static void equipItem(Unit hero, EquippableItem item, UnitEquipment e) {
        EquipmentType type = item.getEquipmentType();
        if (e.getItem(type) == null) {
            StatHelper.increaseStats(hero.getUnitStats(), item.getItemStats());
        } else {
            EquippableItem t = e.getItem(type);
            StatHelper.decreaseStats(hero.getUnitStats(), t.getItemStats());
            StatHelper.increaseStats(hero.getUnitStats(), item.getItemStats());
        }
        e.equipItem(item);
    }
}

