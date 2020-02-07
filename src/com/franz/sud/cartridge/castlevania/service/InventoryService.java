package com.franz.sud.cartridge.castlevania.service;

import com.franz.sud.engine.components.Health;
import com.franz.sud.engine.components.Stats;
import com.franz.sud.engine.components.item.EquippableItem;
import com.franz.sud.engine.components.item.Inventory;
import com.franz.sud.engine.components.item.Item;
import com.franz.sud.engine.components.item.UnitEquipment;
import com.franz.sud.engine.components.unit.SkilledUnit;
import com.franz.sud.engine.misc.EquipmentType;
import com.franz.sud.engine.misc.StatType;
import com.franz.sud.engine.util.StatHelper;

import java.util.List;
import java.util.Scanner;

import static com.franz.sud.engine.misc.TextColor.*;
import static com.franz.sud.engine.util.Sleep.sleep;

public class InventoryService {

    private Inventory gameInventory = new Inventory();
    private Inventory heroInventory = new Inventory();
    private UnitEquipment equipment = new UnitEquipment();
    private SkilledUnit hero;

    public void setHero(SkilledUnit hero) {
        this.hero = hero;
    }

    public void addItemToGameInventory(EquippableItem item) {
        gameInventory.addItem(item);
    }

    /**
     * Adds an item to the hero's inventory if the item is in the game inventory and the hero
     * does not have the item yet.
     * @param item
     */
    public void addItemToHeroInventory(EquippableItem item) {
        if (gameInventory.contains(item)) {
            if (heroInventory.contains(item)) return;
            heroInventory.addItem(item);
            sleep(1);
            System.out.println(ANSI_GREEN + "You found an item" + ANSI_BLACK);
            sleep(1);
            System.out.println(item.getName() + " is added in your inventory");
            sleep(3);
        }
    }

    /**
     * Opens the Inventory menu.
     */
    public void openInventoryMenu() {
        openHeroInventory();
        openHeroEquippedItems();
        System.out.println();
        System.out.println("[E] Exit");
        System.out.println("[I] Inspect Item");
        System.out.println("[U] Equip Item");

        boolean canOpenInventory = true;
        do {
            Scanner sc = new Scanner(System.in);
            switch (sc.nextLine().toLowerCase()) {
                case "e":
                    canOpenInventory = false;
                    break;
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
                    openInventoryMenu();
            }
        } while (canOpenInventory);
    }

    /**
     * Prints the damage, health boost and stats increased by the item.
     * @param index
     */
    private void inspectItem(int index) {
        if (heroInventory.getItem(index) == null) {
            System.out.println(ANSI_RED + "Sorry wrong input or item does not exist" + ANSI_BLACK);
            openInventoryMenu();
        }
        EquippableItem item = heroInventory.getItem(index);
        System.out.println();
        System.out.println("Name: " + item.getName());
        System.out.println("Type: " + item.getEquipmentType());
        System.out.println(ANSI_PURPLE + "Item Boost");
        if (item.getDamage() > 0 ) System.out.println("Damage: +" + item.getDamage());
        if (item.getHealthBoost() > 0 ) System.out.println("Health Boost: +" + item.getHealthBoost());

        Stats s = item.getItemStats();
        for (StatType statType: StatType.values()) {
            if (s.getStatValue(statType) > 0 ) System.out.println(statType.toString() + ": +" + s.getStatValue(statType));
        }
        System.out.println(ANSI_BLACK);
        openHeroEquippedItems();
    }

    /**
     * Uses the item specified (index). Increases the hero's variables by the item's
     * itemStats, damage and healthBoost. If an item is equipped previously, it will
     * decrease the hero's variables by the item's corresponding variables.
     * @param index
     */
    private void useItem(int index){
        // Validates the user's input
        if (heroInventory.getItem(index) == null) {
            System.out.println("Sorry wrong input or item does not exist");
            openInventoryMenu();
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
            h.increaseMaxHealth(item.getHealthBoost());
        }

        if (item.getDamage() > 0) {
            hero.setDamage(hero.getDamage() + item.getDamage());
            System.out.println(ANSI_GREEN + "Damage: +" + item.getDamage());
        }
        sleep(2);
        openHeroEquippedItems();
    }

    /**
     * Shows the current inventory of the hero.
     * @throws NullPointerException
     */
    private void openHeroInventory() {
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

    /**
     * Shows the currently equipped items of the hero
     */
    private void openHeroEquippedItems() {

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
}
