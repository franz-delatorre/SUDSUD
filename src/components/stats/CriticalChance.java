package components.stats;

import utilities.RandomGenerator;

public class CriticalChance  extends Stats{
    public CriticalChance() {
        super();
    }

    @Override
    public int statEffect() {
        return RandomGenerator.lessThan(getStats());
    }
}
