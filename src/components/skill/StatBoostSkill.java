package components.skill;

import components.Stats;
import components.unit.Unit;
import util.StatHelper;
import misc.StatType;

public class StatBoostSkill extends Skill {
    private Stats skillStats;

    public static class Builder {
        private String name;
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

        public StatBoostSkill build() {
            return new StatBoostSkill(this);
        }
    }

    private StatBoostSkill(Builder builder) {
        skillStats = builder.skillStats;
        setName(builder.name);
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
    public void skillAfterEffect(Unit unit) {
        StatHelper.decreaseStats(unit.getUnitStats(), this.skillStats);
    }
}
