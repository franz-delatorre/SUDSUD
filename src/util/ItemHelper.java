package util;

import components.unit.Unit;
import components.item.Item;

public final class ItemHelper {

    private ItemHelper() {

    }

    /**
     * Increases the unit's individual stats by the item's corresponding
     * stat.
     * @param unit
     * @param item
     */
    public static void equipItem(Unit unit, Item item) {
        StatHelper.increaseStats(unit.getUnitStats(), item.getItemStats());
    }

    /**
     * Removes the stat buff of the item from the unit's stats.
     * @param unit
     */
    public static void unequipItem(Unit unit, Item item) {
        StatHelper.decreaseStats(unit.getUnitStats(), item.getItemStats());
    }
}
