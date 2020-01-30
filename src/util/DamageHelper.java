package util;

import components.Health;

import java.util.Random;

public final class DamageHelper {

    /**
     * Returns the damage between the minimum damage and maximum damage
     * @param minDamage
     * @param maxDamage
     * @return
     */
    public static int damageOutput(int minDamage, int maxDamage) {
        int damage = (int) (Math.random() * ((maxDamage - minDamage) + 1)) + minDamage;
        return damage;
    }

    public static void doDamage(Health health, int damage) {
        int ch = health.getCurrentHealth();

        if (ch - damage < 0 ) {
            health.setCurrentHealth(0);
        } else {
            health.setCurrentHealth(ch - damage);
        }
    }
}
