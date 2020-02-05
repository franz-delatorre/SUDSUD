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

    public void takeDamage(int damage) {
        if (currentHealth - damage < 0) {
            currentHealth = 0;
            return;
        }
        currentHealth -= damage;
    }

    public void increaseCurrentHealth(int hpValue) {
        if (currentHealth + hpValue > maxHealth) currentHealth = maxHealth;
        else currentHealth += hpValue;
    }

    public void decreaseCurrentHealth(int hpValue) {
        if (currentHealth - hpValue < 0) currentHealth = 0;
        else currentHealth -= hpValue;
    }

    public int getCurrentHealth() {
        return currentHealth;
    }

    public void setCurrentHealth(int currentHealth) {
        this.currentHealth = currentHealth;
    }
}
