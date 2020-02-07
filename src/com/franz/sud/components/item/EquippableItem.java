package com.franz.sud.components.item;

import com.franz.sud.misc.EquipmentType;

public class EquippableItem extends Item {
    private EquipmentType equipmentType;

    //Builder Class
    public static class Builder extends Item.Builder<Builder> {
        private EquipmentType equipmentType;

        public Builder(){
        }

        public Builder equipmentType(EquipmentType type) {
            equipmentType = type;
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
        equipmentType = builder.equipmentType;
    }

    public EquipmentType getEquipmentType() {
        return equipmentType;
    }
}
