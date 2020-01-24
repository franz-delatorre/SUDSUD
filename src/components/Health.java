package components;

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

    public int getCurrentHealth() {
        return currentHealth;
    }

    public void setCurrentHealth(int currentHealth) {
        this.currentHealth = currentHealth;
    }
}
