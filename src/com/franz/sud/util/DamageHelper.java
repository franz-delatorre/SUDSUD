package com.franz.sud.util;

import com.franz.sud.components.Health;

public final class DamageHelper {

    /**
     * Returns the damage between the minimum damage and maximum damage
     * @param minDamage
     * @param maxDamage
     * @return
     */
    public static int damageOutput(int minDamage, int maxDamage) {
        return (int) (Math.random() * ((maxDamage - minDamage) + 1)) + minDamage;
    }

    /**
     * Decreases the current health by the damage given. If the currentHealth - damage
     * is < 0 the currentHealth is set to 0.
     * @param health
     * @param damage
     */
    public static void doDamage(Health health, int damage) {
        health.decreaseCurrentHealth(damage);
    }
}
