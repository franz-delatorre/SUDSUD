package components.item;

import components.stats.Stats;
import misc.EquipmentType;

public abstract class Item {
    private String name;
    private Stats itemStats;
    private int healthBoost;
    private int damage;
    private EquipmentType itemType;

    public EquipmentType getItemType() {
        return itemType;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setItemStats(Stats itemStats) {
        this.itemStats = itemStats;
    }

    public void setItemType(EquipmentType itemType) {
        this.itemType = itemType;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public void setHealthBoost(int healthBoost) {
        this.healthBoost = healthBoost;
    }

//    public void setStats(StatType statType, int statValue) {
//        itemStats.increaseStat(statType, statValue);
//    }

    public String getName() {
        return name;
    }

    public Stats getItemStats() {
        return itemStats;
    }

    public int getHealthBoost() {
        return healthBoost;
    }

    public int getDamage() {
        return damage;
    }
}
