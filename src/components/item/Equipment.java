package components.item;

import misc.EquipmentType;

import java.util.HashMap;
import java.util.Map;

public class Equipment {
    public Map<EquipmentType, Item> equipment = new HashMap<>();

    public void addEquipment(Item item) {
        equipment.put(item.getItemType(), item);
    }

    public Item getItem(EquipmentType itemType) {
        return equipment.get(itemType);
    }
}
