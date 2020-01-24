package components.item;

import components.Stats;
import misc.EquipmentType;
import misc.StatType;

public abstract class Item {
    private String name;
    private Stats itemStats;
    private int healthBoost;
    private int damage;

    protected void setName(String name) {
        this.name = name;
    }

    protected void setItemStats(Stats itemStats) {
        this.itemStats = itemStats;
    }

    protected void setDamage(int damage) {
        this.damage = damage;
    }

    protected void setHealthBoost(int healthBoost) {
        this.healthBoost = healthBoost;
    }
}
