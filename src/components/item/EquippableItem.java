package components.item;

import components.Stats;
import misc.EquipmentType;
import misc.StatType;

public class EquippableItem extends Item {
    private EquipmentType equipmentType;

    //Builder Class
    public static class Builder extends Item.Builder<Builder> {
        private String name;
        private Stats itemStats;
        private int healthBoost;
        private int damage;
        private EquipmentType equipmentType;

        public Builder(String name){
            this.name = name;
            itemStats = new Stats();
            healthBoost = 0;
            damage = 0;
        }

        public Builder equipmentType(EquipmentType type) {
            equipmentType = type;
            return this;
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
        protected Builder self() {
            return this;
        }

        public EquippableItem build() {
            return new EquippableItem(this);
        }
    }

    private EquippableItem(Builder builder){
        super(builder);
    }

    public EquipmentType getEquipmentType() {
        return equipmentType;
    }
}
