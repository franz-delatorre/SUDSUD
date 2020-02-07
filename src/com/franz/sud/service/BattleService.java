package com.franz.sud.service;

import com.franz.sud.components.Stats;
import com.franz.sud.components.skill.Skill;
import com.franz.sud.components.skill.StatBoostSkill;
import com.franz.sud.components.unit.SkilledUnit;
import com.franz.sud.components.unit.Unit;
import com.franz.sud.misc.StatType;
import com.franz.sud.util.*;

import java.util.Scanner;

import static com.franz.sud.misc.TextColor.*;
import static com.franz.sud.util.Sleep.sleep;

public class BattleService {
    private static final Scanner scanner = new Scanner(System.in);

    private SkilledUnit hero;
    private Unit enemy;

    public BattleService(SkilledUnit hero) {
        this.hero = hero;
    }

    /**
     * Enables the unit to mimic a battle. Player and computer will take turns
     * in during the battle. If one of the units currentHealth reaches to 0 the
     * battle will end.
     *
     * @return 1 if the player wins, and 0 if otherwise.
     */
    public int toBattle(Unit enemyUnit) {
        enemy = enemyUnit;
        setHealth();
        setCooldown();

        int turn = 1;
        while (fightersStillAlive()) {
            sleep(1);
            System.out.println("__________________________________");
            printHealth();
            printCooldown();
            battleIO();
            sleep(1);
            printHealth();
            sleep(2);
            if (!fightersStillAlive()) break;
            System.out.println();
            enemyAI();
            sleep(1);
            printHealth();
            decrementCooldown();
            checkActiveBoostSkill(hero);
            checkActiveBoostSkill(enemy);
            System.out.println(ANSI_BLUE + " - END OF TURN " + turn++ + " - " + ANSI_BLACK);
            System.out.println("__________________________________");
            sleep(1);
        }
        return winner();
    }

    /**
     * Decreases both units skill cooldown by 1.
     */
    private void decrementCooldown() {
        Skill heroSkill = hero.getSkill();
        if (heroSkill.getCooldown() > 0) heroSkill.setCooldown(heroSkill.getCooldown() - 1);
        if (enemy instanceof SkilledUnit) {
            Skill enemySkill = ((SkilledUnit) enemy).getSkill();
            if (enemySkill.getCooldown() > 0) enemySkill.setCooldown(enemySkill.getCooldown() - 1);
        }
    }

    /**
     * Sets both units skill cooldown to 3 turns. If the skill is an instance of
     * StatBoostSkill then the cooldown is set to 6.
     */
    private void setCooldown() {
        Skill heroSkill = hero.getSkill();
        heroSkill.setCooldown(2);
        if (enemy instanceof SkilledUnit) {
            Skill enemySkill = ((SkilledUnit) enemy).getSkill();
            enemySkill.setCooldown(2);
        }
    }

    /**
     * Receives the input of the user's action during the battle. [A] for dealing
     * a normal attack and [S] for using a skill. Skill will not be used if the
     * player's skill is on cooldown.
     */
    public void battleIO() {
        System.out.println();
        System.out.println("[A] normal attack");

        if (hero.getSkill().getCooldown() <= 0) {
            System.out.println("[S] use skill");
        }

        switch (scanner.nextLine().toLowerCase()) {
            case "a":
                normalAttack(hero, enemy);
                break;
            case "s":
                if (hero.getSkill().getCooldown() <= 0) {
                    useSkill(hero, enemy);
                } else {
                    System.out.println("Wrong input try again.");
                    battleIO();
                }
                break;
            default:
                System.out.println("Wrong input try again.");
                battleIO();
        }
    }

    private void useSkill(SkilledUnit user, Unit victim) {
        Skill skill = user.getSkill();
        System.out.println(ANSI_CYAN + user.getName() + " used " + skill.getName() + ANSI_BLACK);
        SkillHelper.useSkill(user, victim);
    }

    /**
     * Prints the player's and enemy's current health and max health
     */
    private void printHealth() {
        System.out.println(hero.getName() + " \t\t\t\t\t"+ enemy.getName());
        System.out.print(hero.getCurrentHealth() + "/" + hero.getMaxHealth());
        System.out.println("\t\t\t\t\t" + enemy.getCurrentHealth() + "/" + enemy.getMaxHealth());
    }

    private void printCooldown() {
        System.out.print("Cd: " + hero.getSkill().getCooldown() + " \t\t\t\t\t");
        if (enemy.getClass() == SkilledUnit.class) {
            System.out.print("Cd: " + ((SkilledUnit) enemy).getSkill().getCooldown());
        }
    }

    /**
     * Sets both units current health to their corresponding max health.
     */
    private void setHealth() {
        hero.getHealth().setCurrentHealth(hero.getMaxHealth());
        enemy.getHealth().setCurrentHealth(enemy.getHealth().getMaxHealth());
    }

    /**
     * Checks if both units current health is greater than zero.
     * @return
     */
    private boolean fightersStillAlive() {
        return hero.isAlive() && enemy.isAlive();
    }

    /**
     * Checks the winner of the game. Will return 1 if the enemy's current
     * health reaches zero, and returns zero if the player's health is zero
     * @return
     */
    private int winner() {
        return (hero.isAlive()) ? 1: 0;
    }

    /**
     * Implements a normal attack simulation. Will exit if the {@link BattleService#canEvade(Stats)}
     * returns true. Attack damage is between the min damage and the max damage of the attacker. Damage is
     * doubled if the {@link BattleService#canCrit(Stats)} method returns true. Will also increase the
     * the health of the attacker based on the lifesteal rate * damage. If health replenish is greater
     * than the max health, the attacker's current health is set to the max health value.
     * @param attacker
     * @param victim
     */
    private void normalAttack(Unit attacker, Unit victim) {
        System.out.println(attacker.getName() + " used normal attack");

        sleep(1);
        //Will return if the attacker can evade the attack
        if (canEvade(victim.getUnitStats())) {
            System.out.println(victim.getName() + " evaded " + attacker.getName() + "'s attack");
            return;
        }

        int damage = DamageHelper.damageOutput(attacker.getMinDamage(), attacker.getDamage());

        //Sets the damage to 2x the damage dealt if canCrit() is true
        Stats stat = attacker.getUnitStats();
        if (canCrit(stat)) {
            sleep(1);
            System.out.println(ANSI_RED + "CRIT!!");
            damage += damage;
        }
        sleep(1);

        // decreases the victim's hp
        DamageHelper.doDamage(victim.getHealth(), damage);
        System.out.println(ANSI_PURPLE + damage + " damage" + ANSI_BLACK);

        double lsValue = (double) stat.getStatValue(StatType.LIFESTEAL);
        int currHealth = attacker.getHealth().getCurrentHealth();
        int maxHealth = attacker.getHealth().getMaxHealth();
        if (lsValue > 0 && currHealth < maxHealth) {
            sleep(1);
            int ls = (int) (damage * (lsValue / 100.0f));
            System.out.println(ANSI_RESET + "lifesteal" + ANSI_BLACK);
            HealthHelper.heal(attacker.getHealth(), ls);
        }
        sleep(1);
    }

    /**`
     * Will generate a chance for doing a critical attack
     * @param stats
     * @return
     */
    private boolean canCrit(Stats stats) {
        return NumberGenerator.getRandomInt(100) <= stats.getStatValue(StatType.CRITICAL_CHANCE);
    }

    private boolean canEvade(Stats stats) {
        return  NumberGenerator.getRandomInt(100) <= stats.getStatValue(StatType.EVASION);
    }

    /**
     * Enables the enemy to do an attack to the player. If the Skill cooldown is
     * greater than zero then the enemy will do a normal attack. All instance
     * of the UnskilledUnit will only do a normal.
     */
    private void enemyAI() {
        //Checks if the enemy have a skill
        if (enemy instanceof SkilledUnit) {
            SkilledUnit unit = (SkilledUnit) enemy;
            Skill skill = unit.getSkill();
            if (skill.getCooldown() <= 0) {
                useSkill((SkilledUnit) enemy, hero);
            } else {
                normalAttack(enemy, hero);
            }
            return;
        }
        normalAttack(enemy,hero);
    }

    /**
     * Checks if there's a boost currently active for the unit. Active boost gets decremented if
     * duration is > 0.
     */
    private void checkActiveBoostSkill(Unit unit) {
        if (!(unit instanceof SkilledUnit)) return;

        SkilledUnit skilledUnit = (SkilledUnit) unit;
        Skill skill = skilledUnit.getSkill();

        if (skill instanceof StatBoostSkill) {
            StatBoostSkill sb = (StatBoostSkill) skill;
            if (sb.getDuration() > 0) decrementBoostDuration(sb, unit);
        }
    }

    /**
     * Decrements the boost by 1. If the duration is equal to 0, {@link StatBoostSkill#skillAfterEffect(Unit)}
     * is called and sets the duration to 2.
     * @param skill
     * @param unit
     */
    private void decrementBoostDuration(StatBoostSkill skill, Unit unit) {
        skill.decreaseDuration();
        if (skill.getDuration() == 0) {
            skill.skillAfterEffect(unit);
        }
        System.out.println(skill.getDuration() + "DURATION");
    }
}
