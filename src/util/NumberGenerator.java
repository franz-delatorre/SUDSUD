package util;

import java.util.Random;

public final class NumberGenerator {
    private static Random random = new Random();

    public static int getRandomInt(int max) {
        return random.nextInt(max);
    }
}
