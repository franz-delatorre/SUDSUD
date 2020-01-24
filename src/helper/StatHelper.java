package helper;

import components.Stats;
import misc.StatType;

public class StatHelper {

    /**
     * Increases the mainStats by the subStats values.
     * @param mainStats
     * @param subStats
     */
    public static void increaseStats(Stats mainStats, Stats subStats) {
        for (StatType statType: StatType.values()) {
            mainStats.increaseStat(statType, subStats.getStatValue(statType));
        }
    }

    /**
     * decreases the mainStats by the subStats value.
     * @param mainStats
     * @param subStats
     */
    public static void decreaseStats(Stats mainStats, Stats subStats) {
        for (StatType statType: StatType.values()) {
            mainStats.decreaseStat(statType, subStats.getStatValue(statType));
        }
    }
}
