package components.skill;

import components.unit.Unit;

public abstract class Booster extends Skill {
    private int duration;

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getDuration() {
        return duration;
    }

    public abstract void skillAfterEffect(Unit unit);
}
