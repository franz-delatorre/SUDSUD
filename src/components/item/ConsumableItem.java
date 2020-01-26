package components.item;

import components.Stats;
import components.item.Item;
import misc.EquipmentType;
import misc.StatType;
import util.StatHelper;

public class ConsumableItem extends Item {

    private boolean isConsumed;

    //Builder Class
    public static class Builder extends Item.Builder {
        private String name;
        private Stats itemStats;
        private int healthBoost;
        private int damage;
        private EquipmentType itemType;

        public Builder(String name, EquipmentType equipmentType){
            this.name = name;
            itemStats = new Stats();
            healthBoost = 0;
            damage = 0;
            itemType = equipmentType;
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

        @Override
        protected Object self() {
            return this;
        }

        public ConsumableItem build() {
            return new ConsumableItem(this);
        }
    }

    private ConsumableItem(Builder builder){
        super(builder);
        setName(builder.name);
        setDamage(builder.damage);
        setHealthBoost(builder.healthBoost);
        setItemStats(builder.itemStats);
    }

    public boolean isUsed() {
        return isConsumed;
    }

    public void useItem(Stats stats) {
        isConsumed = true;
        StatHelper.increaseStats(stats, getItemStats());
    }
}
