package com.franz.sud.engine.components.skill;

import com.franz.sud.engine.components.Health;
import com.franz.sud.engine.components.unit.Unit;
import com.franz.sud.misc.Broadcaster;
import com.franz.sud.engine.util.DamageHelper;

public class DamageSkill extends Skill {
    private int damage;

    public DamageSkill(String name, int damage) {
        this.damage = damage;
        setName(name);
    }

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    /**
     * Deals damage to the opponent. The damage dealt is based on
     * the damage set
     * @param user
     * @param victim
     */
    @Override
    public void skillEffect(Unit user, Unit victim) {
        Health victimHealth  = victim.getHealth();
        DamageHelper.doDamage(victimHealth, damage);
        Broadcaster.relayDamage(damage);
    }
}
