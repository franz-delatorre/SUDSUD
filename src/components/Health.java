package components;

import java.security.PublicKey;

public class Health {
    private int maxHealth;
    private int currentHealth;

    public Health(int health) {
        this.maxHealth     = health;
        this.currentHealth = health;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public void setMaxHealth(int maxHealth) {
        this.maxHealth = maxHealth;
    }

    public void takeDamage(int damage) {
        currentHealth -= damage;
    }

    public int getCurrentHealth() {
        return currentHealth;
    }

    public void setCurrentHealth(int currentHealth) {
        this.currentHealth = currentHealth;
    }

    public Health clone() {
        return new Health(maxHealth);
    }
}
