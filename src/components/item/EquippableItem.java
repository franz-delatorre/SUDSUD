package components.item;

import components.Stats;
import misc.EquipmentType;
import misc.StatType;

public class EquippableItem extends Item {
    public static class Builder {
        private String name;
        private Stats itemStats;
        private int healthBoost;
        private int damage;
        private EquipmentType itemType;

        public Builder(String name){
            this.name = name;
            itemStats = new Stats();
            healthBoost = 0;
            damage = 0;
        }

        public Builder criticalChance(int value) {
            itemStats.increaseStat(StatType.CRITICAL_CHANCE, value);
            return this;
        }

        public Builder evasion(int value) {
            itemStats.increaseStat(StatType.EVASION, value);
            return this;
        }

        public Builder lifesteal(int value) {
            itemStats.increaseStat(StatType.LIFESTEAL, value);
            return this;
        }

        public Builder health(int value) {
            healthBoost = value;
            return this;
        }

        public Builder damage(int value) {
            damage = value;
            return this;
        }

        public EquippableItem build() {
            return new EquippableItem(this);
        }
    }

    private EquippableItem(Builder builder){
        setName(builder.name);
        setDamage(builder.damage);
        setHealthBoost(builder.healthBoost);
        setItemStats(builder.itemStats);
    }
}
