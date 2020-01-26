package util;

import components.unit.SkilledUnit;
import components.unit.Unit;

public final class SkillHelper {

    private SkillHelper() {

    }

    public static void useSkill(SkilledUnit user, Unit victim) {
        user.getSkill().skillEffect(user, victim);
    }
}
