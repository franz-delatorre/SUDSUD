package components.item;

import java.util.ArrayList;
import java.util.List;

public class Inventory {
    private List<EquippableItem> inventory = new ArrayList<>();

    public void addItem(EquippableItem item) {
        inventory.add(item);
    }

    public List<EquippableItem> getInventory() {
        return inventory;
    }

    public EquippableItem getItem(int index){
        if (index > inventory.size()) {
            return null;
        }
        return inventory.get(index - 1);
    }

    public boolean contains(EquippableItem item) {
        return inventory.contains(item);
    }
}
