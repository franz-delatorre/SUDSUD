package com.franz.sud.components.skill;

import com.franz.sud.components.unit.Unit;
import com.franz.sud.misc.Broadcaster;
import com.franz.sud.util.DamageHelper;

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
