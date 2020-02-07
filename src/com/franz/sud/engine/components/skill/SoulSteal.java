package com.franz.sud.engine.components.skill;

import com.franz.sud.engine.components.unit.Unit;
import com.franz.sud.engine.misc.Broadcaster;
import com.franz.sud.engine.util.DamageHelper;
import com.franz.sud.engine.util.HealthHelper;

public class SoulSteal extends Skill {
    public SoulSteal(){
        setName("Soul Steal");
    }

    /**
     * Deals 2 times the maximum damage of the user and heals the user
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
