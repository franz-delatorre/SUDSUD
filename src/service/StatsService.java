package service;

import components.stats.Stats;

public class StatsService {
    private Stats stats;

    public void setStats(Stats stats) {
        this.stats = stats;
    }

    public int getStatsEffect() {
        return stats.statEffect();
    }

    public void increaseStats(int statValue) {
        stats.increaseStat(statValue);
    }

    public void decreasseStats(int statValue) {
        stats.decreaseStat(statValue);
    }
}
