package com.franz.sud.engine.util;

import com.franz.sud.engine.components.Health;
import com.franz.sud.misc.TextColor;

public final class HealthHelper {

    private HealthHelper() {

    }

    /**
     * Increases the current health by the heal value given.
     * @param health
     * @param heal
     */
    public static void heal(Health health, int heal) {
        int currentHealth = health.getCurrentHealth();
        if (currentHealth + heal > health.getMaxHealth()) {
            int max = health.getMaxHealth();
            System.out.println(TextColor.ANSI_GREEN + "+" + (max - currentHealth) + " health" + TextColor.ANSI_BLACK);
        } else {
            System.out.println(TextColor.ANSI_GREEN + "+" + heal + " health" + TextColor.ANSI_BLACK);
        }
        health.increaseCurrentHealth(heal);
    }
}
