package components.skill.list;

import components.Health;
import components.skill.Skill;
import components.unit.Unit;
import helper.HealthHelper;

public class SoulSteal extends Skill {
    public SoulSteal(){
        setName("Soul Steal");
    }

    @Override
    public void skillEffect(Unit user, Unit victim) {
        int damage = user.getDamage() * 2;

        HealthHelper.takeDamage(victim.getHealth(), damage);
        HealthHelper.heal(user.getHealth(), damage);
    }
}
