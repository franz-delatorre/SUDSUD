package game.engine;

import components.Stats;
import components.skill.Skill;
import components.unit.SkilledUnit;
import components.unit.Unit;
import misc.StatType;
import misc.TextColor;
import util.*;

import java.util.Scanner;

public class BattleManager {
    private static final Scanner scanner = new Scanner(System.in);
    private static final String BLACK = TextColor.ANSI_BLACK;
    private static final String CYAN = TextColor.ANSI_CYAN;
    private static final String RED = TextColor.ANSI_RED;
    private static final String PURPLE = TextColor.ANSI_PURPLE;
    private static final String BLUE = TextColor.ANSI_BLUE;
    private static final String GREEN = TextColor.ANSI_GREEN;

    private SkilledUnit hero;
    private Unit enemy;

    public BattleManager(SkilledUnit hero) {
        this.hero = hero;
    }

    public void setEnemy(Unit enemy) {
        this.enemy = enemy;
    }

    /**
     * Enables the unit to mimic a battle. Player and computer will take turns
     * in during the battle. If one of the units currentHealth reaches to 0 the
     * battle will end. Returns 1 if the player wins, and 0 if otherwise.
     * @return
     */
    public int toBattle() {
        setHealth();
        setCooldown();

        int turn = 1;
        while (fightersStillAlive()) {
            Sleep.sleep(1);
            System.out.println("__________________________________");
            printHealth();
            printCooldown();
            battleIO();
            Sleep.sleep(1);
            printHealth();
            Sleep.sleep(2);
            if (!fightersStillAlive()) break;
            System.out.println();
            enemyAI();
            Sleep.sleep(1);
            printHealth();
            decrementCooldown();
            System.out.println(BLUE + " - END OF TURN " + turn++ + " - " + BLACK);
            System.out.println("__________________________________");
            Sleep.sleep(1);
        }
        return winner();
    }

    /**
     * Decreases both units skill cooldown by 1.
     */
    private void decrementCooldown() {
        Skill heroSkill = hero.getSkill();
        if (heroSkill.getCooldown() > 0) heroSkill.setCooldown(heroSkill.getCooldown() - 1);
        if (enemy.getClass() == SkilledUnit.class) {
            Skill enemySkill = ((SkilledUnit) enemy).getSkill();
            if (enemySkill.getCooldown() > 0) enemySkill.setCooldown(enemySkill.getCooldown() - 1);
        }
    }

    /**
     * Sets both units skill cooldown to 3 turns.
     */
    private void setCooldown() {
        hero.getSkill().setCooldown(3);
        if (enemy.getClass() == SkilledUnit.class) ((SkilledUnit) enemy).getSkill().setCooldown(3);
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
        System.out.println(CYAN + user.getName() + " used " + skill.getName() + BLACK);
        SkillHelper.useSkill(user, victim);
    }

    /**
     * Prints both units current health and max health
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
        int heroCurrHealth = hero.getCurrentHealth();
        int enemyCurrHealth = enemy.getCurrentHealth();
        if (heroCurrHealth > 0 && enemyCurrHealth > 0) return true;
        return false;
    }

    /**
     * Checks the winner of the game. Will return 1 if the enemy's current
     * health reaches zero, and returns zero if the player's health is zero
     * @return
     */
    private int winner() {
        if (enemy.getCurrentHealth() <= 0 ) {
            enemy.setIsAlive(false);
            return 1;
        }
        return 0;
    }

    /**
     *
     * @param attacker
     * @param victim
     */
    private void normalAttack(Unit attacker, Unit victim) {
        System.out.println(attacker.getName() + " used normal attack");

        Sleep.sleep(1);
        //Will return if the attacker can evade the attack
        if (canEvade(victim.getUnitStats())) {
            System.out.println(victim.getName() + " evaded " + attacker.getName() + "'s attack");
            return;
        }

        int damage = DamageHelper.damageOutput(attacker.getMinDamage(), attacker.getDamage());

        //Sets the damage to 2x the damage dealt if canCrit() is true
        Stats stat = attacker.getUnitStats();
        if (canCrit(stat)) {
            Sleep.sleep(1);
            System.out.println(RED + "CRIT!!");
            damage += damage;
        }
        Sleep.sleep(1);

        // decreases the victim's hp
        DamageHelper.doDamage(victim.getHealth(), damage);
        System.out.println(PURPLE + damage + " damage" + BLACK);

        //
        int lsValue = stat.getStatValue(StatType.LIFESTEAL);
        if (lsValue > 0) {
            Sleep.sleep(1);
            double ls = damage * (lsValue / 100);
            System.out.println(GREEN +ls + " lifesteal" + BLACK);
            HealthHelper.heal(attacker.getHealth(), (int) ls);
        }
        Sleep.sleep(1);
    }

    /**`
     * Will generate a chance for doing a critical attack
     * @param stats
     * @return
     */
    private boolean canCrit(Stats stats) {
        if (RandomGenerator.getRandomInt(100) <= stats.getStatValue(StatType.CRITICAL_CHANCE)) return true;
        return false;
    }

    private boolean canEvade(Stats stats) {
        if (RandomGenerator.getRandomInt(100) <= stats.getStatValue(StatType.EVASION)) return true;
        return false;
    }

    private void lifesteal(Stats stats, int damage) {
    }

    /**
     * Enables the enemy to do an attack to the player. If the Skill cooldown is
     * greater than zero then the enemy will do a normal attack. All instance
     * of the UnskilledUnit will only do a normal.
     */
    private void enemyAI() {
        //Checks if the enemy have a skill
        if (enemy.getClass() == SkilledUnit.class) {
            Skill skill = ((SkilledUnit) enemy).getSkill();
            if (skill.getCooldown() <= 0) {
                useSkill((SkilledUnit) enemy, hero);
            } else {
                normalAttack(enemy, hero);
            }
            return;
        }
        normalAttack(enemy,hero);
    }
}
