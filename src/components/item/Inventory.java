package components.item;

import java.util.ArrayList;
import java.util.List;

public class Inventory {
    private List<Item> inventory = new ArrayList<>();

    public void addItem(Item item) {
        inventory.add(item);
    }
}
