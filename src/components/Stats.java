package components;

import misc.StatType;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

public class Stats {
    private Map<StatType, Integer> stats = new EnumMap<StatType, Integer>(StatType.class);

    public Stats() {
        stats.put(StatType.EVASION, 0);
        stats.put(StatType.CRITICAL_CHANCE, 0);
        stats.put(StatType.LIFESTEAL, 0);
    }

    /**
     * Increases the Stat Type by the stat Value given. If the
     * Stat is >= 100 then the stat is set to 99.
     * @param statType
     * @param statValue
     */
    public void increaseStat(StatType statType, int statValue){
        if (stats.get(statType) + statValue >= 100) {
            stats.put(statType, 99);
            return;
        }

        int currentStatValue = stats.get(statType);
        int newStatValue     = currentStatValue + statValue;
        stats.put(statType, newStatValue);
    }

    /**
     * Decreases the Stat Type by the stat Value given. If the
     * stat is < 0 then the stat is set to 0.
     * @param statType
     * @param statValue
     */
    public void decreaseStat(StatType statType, int statValue){

        int currentStatValue = stats.get(statType);
        int newStatValue     = currentStatValue - statValue;
        stats.put(statType, newStatValue);
    }

    public int getStatValue(StatType statType) {
        return stats.get(statType);
    }
}
