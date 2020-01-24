package components.skill.list;

import components.Health;
import components.skill.Skill;
import components.unit.Unit;

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

    @Override
    public void skillEffect(Unit user, Unit victim) {
        Health victimHealth  = victim.getHealth();
        int victimCurrHealth = victimHealth.getCurrentHealth();
        victimHealth.setCurrentHealth(victimCurrHealth - this.damage);
    }
}
