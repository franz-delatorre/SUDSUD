package util;

import java.util.Random;

public final class DamageHelper {

    public static int damageOutput(int maxDamage) {
        int minDamage = (int) (maxDamage * .1);
        int damage = (int) (Math.random() * ((maxDamage - minDamage) + 1)) + minDamage;
        return damage + minDamage;
    }
}
