package com.franz.sud.util;

import com.franz.sud.components.skill.Skill;
import com.franz.sud.components.skill.StatBoostSkill;
import com.franz.sud.components.unit.SkilledUnit;
import com.franz.sud.components.unit.Unit;

public final class SkillHelper {

    private SkillHelper() {

    }

    public static void useSkill(SkilledUnit user, Unit victim) {
        Skill skill = user.getSkill();
        skill.skillEffect(user, victim);
        skill.setCooldown(4);
        if (skill instanceof StatBoostSkill) {
            StatBoostSkill sb = (StatBoostSkill) skill;
            skill.setCooldown(7);
            sb.setDuration(4);
        }
    }
}
