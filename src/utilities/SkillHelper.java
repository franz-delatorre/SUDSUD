package utilities;

import components.unit.SkilledUnit;
import components.unit.Unit;

public class SkillHelper {
    public static void useSkill(SkilledUnit user, Unit victim) {
        user.getSkill().skillEffect(user, victim);
    }
}
