package components.unit;

import components.Health;
import components.Stats;
import misc.StatType;

public abstract class Unit {
    private String name;
    private Health health;
    private int damage;
    private Stats unitStats;

    abstract static class Builder<T extends Builder<T>>{
        private String name;
        private Health health;
        private int damage;
        private Stats unitStats;

        public Builder(){
            damage = 0;
            unitStats = new Stats();
        }

        public T name(String name) {
            this.name = name;
            return self();
        }

        public T evasion(int value) {
            unitStats.increaseStat(StatType.EVASION, value);
            return self();
        }

        public T lifesteal(int value) {
            unitStats.increaseStat(StatType.LIFESTEAL, value);
            return self();
        }

        public T criticalChance(int value) {
            unitStats.increaseStat(StatType.CRITICAL_CHANCE, value);
            return self();
        }

        public T health(int hpValue) {
            health = new Health(hpValue);
            return self();
        }

        public T damage(int damage) {
            this.damage = damage;
            return self();
        }
        abstract Unit build();

        protected abstract T self();
    }

    Unit(Builder<?> builder) {
        name = builder.name;
        health = builder.health;
        damage = builder.damage;
        unitStats = builder.unitStats;
    }

    /**
     * Minimum damage is 10% of the current damage.
     * @return
     */
    public int getMinDamage() {
        double minDamage = this.damage * .9;
        return (int) minDamage;
    }

    public boolean isAlive() {
        return health.isAlive();
    }

    public Stats getUnitStats() {
        return unitStats;
    }

    public String getName() {
        return name;
    }

    public Health getHealth() {
        return health;
    }

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public int getCurrentHealth() {
        return health.getCurrentHealth();
    }

    public int getMaxHealth() {
        return health.getMaxHealth();
    }
}
