package components.skill;

import components.Health;
import components.skill.Skill;
import components.unit.Unit;
import misc.Broadcaster;

public class HealSkill extends Skill {
    private int heal;

    public HealSkill(String name) {
        setName(name);
    }

    public void setHeal(int heal) {
        this.heal = heal;
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
        int userMaxHealth     = user.getHealth().getMaxHealth();
        int userCurrentHealth = user.getHealth().getCurrentHealth();
        Health userhealth     = user.getHealth();

        if (userCurrentHealth + heal > userMaxHealth) {
            userhealth.setCurrentHealth(userMaxHealth);
            Broadcaster.relayHeal(userMaxHealth - userCurrentHealth);
        } else {
            int totalHeal = userCurrentHealth + heal;
            userhealth.setCurrentHealth(totalHeal);
            Broadcaster.relayHeal(heal);
        }
    }
}
