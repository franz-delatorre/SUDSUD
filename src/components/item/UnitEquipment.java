package components.item;

import misc.EquipmentType;

import java.util.HashMap;
import java.util.Map;

public class UnitEquipment {
    public Map<EquipmentType, Item> equipment;

    public UnitEquipment() {
        equipment = new HashMap<>();
    }

    public Item getItem(EquipmentType itemType) {
        return equipment.get(itemType);
    }
}
