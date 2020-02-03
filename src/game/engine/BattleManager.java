package game.engine;

import components.Stats;
import components.skill.Skill;
import components.unit.SkilledUnit;
import components.unit.Unit;
import misc.StatType;
import util.*;

import java.util.Scanner;

import static misc.TextColor.ANSI_BLACK;
import static misc.TextColor.ANSI_BLUE;
import static misc.TextColor.ANSI_CYAN;
import static misc.TextColor.ANSI_RED;
import static misc.TextColor.ANSI_PURPLE;
import static misc.TextColor.ANSI_RESET;

public class BattleManager {
    private static final Scanner scanner = new Scanner(System.in);

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
            System.out.println(ANSI_BLUE + " - END OF TURN " + turn++ + " - " + ANSI_BLACK);
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
     * Implements a normal attack simulation. Will exit if the {@link BattleManager#canEvade(Stats)}
     * returns true. Attack damage is between the min damage and the max damage of the attacker. Damage is
     * doubled if the {@link BattleManager#canCrit(Stats)} method returns true. Will also increase the
     * the health of the attacker based on the lifesteal rate * damage. If health replenish is greater
     * than the max health, the attacker's current health is set to the max health value.
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
            System.out.println(ANSI_RED + "CRIT!!");
            damage += damage;
        }
        Sleep.sleep(1);

        // decreases the victim's hp
        DamageHelper.doDamage(victim.getHealth(), damage);
        System.out.println(ANSI_PURPLE + damage + " damage" + ANSI_BLACK);


        double lsValue = (double) stat.getStatValue(StatType.LIFESTEAL);
        int currHealth = attacker.getHealth().getCurrentHealth();
        int maxHealth = attacker.getHealth().getMaxHealth();
        if (lsValue > 0 && currHealth < maxHealth) {
            Sleep.sleep(1);
            int ls = (int) (damage * (lsValue / 100.0f));
            System.out.println(ANSI_RESET + "lifesteal" + ANSI_BLACK);
            HealthHelper.heal(attacker.getHealth(), ls);
        }
        Sleep.sleep(1);
    }

    /**`
     * Will generate a chance for doing a critical attack
     * @param stats
     * @return
     */
    private boolean canCrit(Stats stats) {
        return RandomGenerator.getRandomInt(100) <= stats.getStatValue(StatType.CRITICAL_CHANCE);
    }

    private boolean canEvade(Stats stats) {
        return  RandomGenerator.getRandomInt(100) <= stats.getStatValue(StatType.EVASION);
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
