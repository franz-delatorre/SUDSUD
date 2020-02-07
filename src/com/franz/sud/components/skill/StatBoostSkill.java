package com.franz.sud.components.skill;

import com.franz.sud.components.Stats;
import com.franz.sud.components.unit.Unit;
import com.franz.sud.util.StatHelper;
import com.franz.sud.misc.StatType;

public class StatBoostSkill extends Skill {
    private Stats skillStats;
    private int duration;

    public static class Builder {
        private String name;
        private int duration;
        private Stats skillStats = new Stats();

        public Builder(String name){
            this.name = name;
        }

        public Builder evasion(int statValue){
            skillStats.increaseStat(StatType.EVASION, statValue);
            return this;
        }

        public Builder lifesteal(int statValue) {
            skillStats.increaseStat(StatType.LIFESTEAL, statValue);
            return this;
        }

        public Builder criticalChance(int statValue) {
            skillStats.increaseStat(StatType.CRITICAL_CHANCE, statValue);
            return this;
        }

        public Builder duration(int duration) {
            this.duration = duration;
            return this;
        }

        public StatBoostSkill build() {
            return new StatBoostSkill(this);
        }
    }

    private StatBoostSkill(Builder builder) {
        skillStats = builder.skillStats;
        duration = builder.duration;
        setName(builder.name);
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public void decreaseDuration() {
        duration--;
    }

    /**
     * Buffs the user's stats for a limited turn
     * @param user
     * @param victim
     */
    @Override
    public void skillEffect(Unit user, Unit victim) {
        duration = 2;
        StatHelper.increaseStats(user.getUnitStats(), this.skillStats);
    }

    /**
     * Debuffs the user's stats if the limited turn is over.
     * @param unit
     */
    public void skillAfterEffect(Unit unit) {
        StatHelper.decreaseStats(unit.getUnitStats(), this.skillStats);
    }
}
