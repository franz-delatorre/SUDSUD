package com.franz.sud.misc;

public enum EquipmentType {
    WEAPON {
        @Override
        public String toString() {
            return "Weapon";
        }
    },
    ARMOR {
        @Override
        public String toString() {
            return "Armor";
        }
    },
    AMULET {
        @Override
        public String toString() {
            return "Amulet";
        }
    };
}
