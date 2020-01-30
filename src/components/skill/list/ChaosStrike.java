package components.skill.list;

import components.skill.Skill;
import components.unit.Unit;
import misc.Broadcaster;
import util.DamageHelper;
import util.HealthHelper;

public class ChaosStrike extends Skill {
    public ChaosStrike() {
        setName("Chaos Strike");
    }

    /**
     * Deals 4 times the maximum damage of the user.
     * @param user
     * @param victim
     */
    @Override
    public void skillEffect(Unit user, Unit victim) {
        int damage = user.getDamage() * 4;
        DamageHelper.doDamage(victim.getHealth(), damage);
        Broadcaster.relayDamage(damage);
    }
}
