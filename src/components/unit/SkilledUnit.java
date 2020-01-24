package components.unit;

import components.skill.Skill;

public class SkilledUnit extends Unit {
    private Skill skill;

    public static class Builder extends Unit.Builder<Builder> {
        private Skill skill;

        public Builder() {
        }

        public Builder setSkill(Skill skill) {
            this.skill = skill;
            return self();
        }

        public SkilledUnit build() {
            return new SkilledUnit(this);
        }

        @Override
        protected Builder self() {
            return this;
        }
    }

    protected SkilledUnit(Builder builder) {
        super(builder);
        skill = builder.skill;
    }

    public Skill getSkill() {
        return skill;
    }

    public void setSkill(Skill skill) {
        this.skill = skill;
    }
}
