package components.stats;

public abstract class Stats {
    private int stats;

    public Stats() {
        this.stats = 0;
    }

    protected int getStats() {
        return stats;
    }

    public abstract int statEffect();

    /**
     * Increases the Stat Type by the stat Value given. If the
     * Stat is >= 100 then the stat is set to 99. Stat value here is
     * based on the percentage of each stats and it limits to 99% only
     * @param statValue
     */
    public void increaseStat(int statValue){
        if (stats + statValue >= 100) {
            stats = 99;
            return;
        }

        stats += statValue;
    }

    /**
     * Decreases the Stat Type by the stat Value given. If the
     * stat is < 0 then the stat is set to 0.
     * @param statValue
     */
    public void decreaseStat(int statValue){
        stats -= statValue;
    }
}
