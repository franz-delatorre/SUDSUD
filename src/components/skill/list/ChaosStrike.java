package components.skill.list;

import components.skill.Skill;
import components.unit.Unit;
import helper.HealthHelper;

public class ChaosStrike extends Skill {
    public ChaosStrike() {
        setName("Chaos Strike");
    }

    @Override
    public void skillEffect(Unit user, Unit victim) {
        int damage = user.getDamage() * 4;
        HealthHelper.takeDamage(victim.getHealth(), damage);
    }
}
