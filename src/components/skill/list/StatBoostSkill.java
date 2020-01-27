package components.skill.list;

import components.Stats;
import components.skill.Booster;
import components.unit.Unit;
import util.StatHelper;
import misc.StatType;

public class StatBoostSkill extends Booster {
    private Stats skillStats;

    public static class Builder {
        private String name;
        private Stats skillStats = new Stats();
        private int duration;

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

        public Builder duration(int value) {
            duration = value;
            return this;
        }

        public StatBoostSkill build() {
            return new StatBoostSkill(this);
        }
    }

    private StatBoostSkill(Builder builder) {
        skillStats = builder.skillStats;
        setName(builder.name);
        setDuration(builder.duration);
    }

    /**
     * Buffs the user's stats for a limited turn
     * @param user
     * @param victim
     */
    @Override
    public void skillEffect(Unit user, Unit victim) {
        StatHelper.increaseStats(user.getUnitStats(), this.skillStats);
    }

    /**
     * Debuffs the user's stats if the limited turn is over.
     * @param unit
     */
    @Override
    public void skillAfterEffect(Unit unit) {
        StatHelper.decreaseStats(unit.getUnitStats(), this.skillStats);
    }
}
