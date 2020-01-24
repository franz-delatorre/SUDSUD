package components.stats;

import utilities.RandomGenerator;

public class Evasion extends Stats {
    public Evasion() {
        super();
    }

    @Override
    public int statEffect() {
        return RandomGenerator.lessThan(getStats());
    }
}
