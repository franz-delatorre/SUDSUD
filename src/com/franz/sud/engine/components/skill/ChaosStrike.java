package com.franz.sud.engine.components.skill;

import com.franz.sud.engine.components.unit.Unit;
import com.franz.sud.engine.misc.Broadcaster;
import com.franz.sud.engine.util.DamageHelper;

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