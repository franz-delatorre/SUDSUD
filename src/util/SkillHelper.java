package util;

import components.skill.Skill;
import components.unit.SkilledUnit;
import components.unit.Unit;

public final class SkillHelper {

    private SkillHelper() {

    }

    public static void useSkill(SkilledUnit user, Unit victim) {
        Skill skill = user.getSkill();
        skill.skillEffect(user, victim);
        skill.setCooldown(4);
    }
}
