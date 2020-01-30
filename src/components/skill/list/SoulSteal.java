package components.skill.list;

import components.skill.Skill;
import components.unit.Unit;
import misc.Broadcaster;
import util.DamageHelper;
import util.HealthHelper;

public class SoulSteal extends Skill {
    public SoulSteal(){
        setName("Soul Steal");
    }

    /**
     * Deals 2 x the maximum damage of the user and heals the user
     * for the same amount of the damage output.
     * @param user
     * @param victim
     */
    @Override
    public void skillEffect(Unit user, Unit victim) {
        int damage = user.getDamage() * 2;

        DamageHelper.doDamage(victim.getHealth(), damage);
        HealthHelper.heal(user.getHealth(), damage);
        Broadcaster.relayDamage(damage);
    }
}
