package utilities;

import components.Health;

public class HealthHelper {

    /**
     * Reduces the current health by the damage value given;
     * @param health
     * @param damage
     */
    public static void takeDamage(Health health, int damage) {
        int currentHealth = health.getCurrentHealth();
        health.setCurrentHealth(currentHealth - damage);
    }

    /**
     * Increases the current health by the heal value given.
     * @param health
     * @param heal
     */
    public static void heal(Health health, int heal) {
        int currentHealth = health.getCurrentHealth();
        if (currentHealth + heal > health.getMaxHealth()) {
            health.setCurrentHealth(health.getCurrentHealth());
        } else {
            health.setCurrentHealth(currentHealth + heal);
        }
    }
}
