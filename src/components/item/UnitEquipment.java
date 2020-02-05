package components.item;

import misc.EquipmentType;

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
