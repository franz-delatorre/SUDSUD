package com.franz.sud.engine.util;

import com.franz.sud.engine.components.Stats;
import com.franz.sud.engine.misc.Broadcaster;
import com.franz.sud.engine.misc.StatType;

public final class StatHelper {

    private StatHelper() {

    }

    /**
     * Increases the mainStats by the subStats values.
     * @param mainStats
     * @param subStats
     */
    public static void increaseStats(Stats mainStats, Stats subStats) {
        for (StatType statType: StatType.values()) {
            mainStats.increaseStat(statType, subStats.getStatValue(statType));
        }
        Broadcaster.relayBuff(subStats);
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
        Broadcaster.relayDebuff(subStats);
    }
}
