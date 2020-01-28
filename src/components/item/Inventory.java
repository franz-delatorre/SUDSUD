package components.item;

import java.util.ArrayList;
import java.util.List;

public class Inventory {
    private List<Item> inventory = new ArrayList<>();

    public void addItem(Item item) {
        inventory.add(item);
    }

    public List<Item> getInventory() {
        return inventory;
    }

    public Item getItem(int index) {
        return inventory.get(index - 1);
    }

    public boolean contains(Item item) {
        return inventory.contains(item);
    }
}
