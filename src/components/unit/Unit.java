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
            damage    = 0;
            unitStats = new Stats();
        }

        public T name(String name) {
            this.name = name;
            return self();
        }

        public T stat(StatType statType, int value) {
            unitStats.increaseStat(statType, value);
            return self();
        }

        public T health(int health) {
            this.health = new Health(health);
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
        name      = builder.name;
        health    = builder.health;
        damage    = builder.damage;
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
}
