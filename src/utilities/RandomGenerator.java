package utilities;

import java.util.Random;

public final class RandomGenerator {
    private static final int STAT_MAX = 100;
    private static final Random random = new Random();

    public static int lessThan(int value) {
        if (random.nextInt(STAT_MAX) <= value) return 1;
        return 0;
    }
}
