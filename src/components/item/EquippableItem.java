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

        public Builder(String name, EquipmentType equipmentType){
            this.name   = name;
            itemStats   = new Stats();
            healthBoost = 0;
            damage      = 0;
            itemType    = equipmentType;
        }

        public Builder stat(StatType statType, int value) {
            itemStats.increaseStat(statType, value);
            return  this;
        }

        public Builder health(int value) {
            healthBoost = value;
            return this;
        }

        public Builder damage(int value) {
            damage = value;
            return this;
        }

        public Builder type(EquipmentType type) {
            itemType = type;
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
        setItemType(builder.itemType);
    }
}
