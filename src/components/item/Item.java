package components.item;

import components.Stats;
import misc.EquipmentType;
import misc.StatType;

public abstract class Item {
    private String name;
    private String description;
    private Stats itemStats;
    private int healthBoost;
    private int damage;

    public static abstract class Builder<T> {
        private String name;
        private String description;
        private Stats itemStats;
        private int healthBoost;
        private int damage;

        public Builder() {
            itemStats = new Stats();
            damage = 0;
            healthBoost = 0;
        }

        public T description(String desc) {
            this.description = desc;
            return self();
        }

        public T healthBoost(int value) {
            healthBoost = value;
            return self();
        }

        public T damage(int value) {
            damage = value;
            return self();
        }

        public T evasion(int value) {
            itemStats.increaseStat(StatType.EVASION, value);
            return self();
        }

        public T criticalChance(int value) {
            itemStats.increaseStat(StatType.CRITICAL_CHANCE, value);
            return self();
        }

        public T lifesteal(int value) {
            itemStats.increaseStat(StatType.LIFESTEAL, value);
            return self();
        }

        public T name(String name) {
            this.name = name;
            return self();
        }

        protected abstract T self();

        abstract Item build();
    }

    Item(Builder builder) {
        name = builder.name;
        itemStats = builder.itemStats;
        damage = builder.damage;
        healthBoost = builder.healthBoost;
    }

    public Stats getItemStats() {
        return itemStats;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

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
