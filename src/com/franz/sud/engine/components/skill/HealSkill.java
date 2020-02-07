package com.franz.sud.engine.components.skill;

import com.franz.sud.engine.components.Health;
import com.franz.sud.engine.components.unit.Unit;
import com.franz.sud.engine.misc.Broadcaster;

public class HealSkill extends Skill {
    private int healValue;

    public HealSkill(String name, int healValue) {
        this.healValue = healValue;
        setName(name);
    }

    /**
     * Replenishes the user's current health by this heal value. If the
     * replenished heal is greater than the max health the current health
     * is set to the value of the max health. Thus limit the healing not
     * greater than max health.
     * @param user
     * @param victim
     */
    @Override
    public void skillEffect(Unit user, Unit victim) {
        Health userHealth = user.getHealth();
        int userMaxHealth = userHealth.getMaxHealth();
        int userCurrentHealth = userHealth.getCurrentHealth();

        if (userCurrentHealth + healValue > userMaxHealth) {
            userHealth.setCurrentHealth(userMaxHealth);
            Broadcaster.relayHeal(userMaxHealth - userCurrentHealth);
        } else {
            userHealth.increaseCurrentHealth(healValue);
            Broadcaster.relayHeal(healValue);
        }
    }
}
