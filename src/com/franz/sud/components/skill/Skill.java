package com.franz.sud.components.skill;

import com.franz.sud.components.unit.Unit;

public abstract class Skill{
    private String name;
    private int cooldown;

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public int getCooldown() {
        return cooldown;
    }

    public void setCooldown(int cooldown) {
        this.cooldown = cooldown;
    }

    public abstract void skillEffect(Unit user, Unit victim);
}
