package com.franz.sud.engine.components.item;

import com.franz.sud.misc.EquipmentType;

import java.util.EnumMap;
import java.util.Map;

public class UnitEquipment {
    public Map<EquipmentType, EquippableItem> equipment;

    public UnitEquipment() {
        equipment = new EnumMap<>(EquipmentType.class);
    }

    public EquippableItem getItem(EquipmentType itemType) {
        return equipment.get(itemType);
    }

    public void equipItem(EquippableItem item) {
        equipment.put(item.getEquipmentType(), item);
    }
}
