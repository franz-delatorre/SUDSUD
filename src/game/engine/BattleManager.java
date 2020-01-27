package game.engine;

import components.Stats;
import components.skill.Skill;
import components.unit.SkilledUnit;
import components.unit.Unit;
import misc.StatType;
import util.DamageHelper;
import util.RandomGenerator;
import util.SkillHelper;

import java.util.Scanner;

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
        while (fightersStillAlive()) {
            printHealth();
            battleIO();
            enemyAI();
            setCooldown();
            decrementCooldown();
        }
        return winner();
    }

    /**
     * Decreases both units skill cooldown by 1.
     */
    private void decrementCooldown() {
        Skill heroSkill = hero.getSkill();
        heroSkill.setCooldown(heroSkill.getCooldown() - 1);
        if (enemy.getClass() == SkilledUnit.class) {
            Skill enemySkill = ((SkilledUnit) enemy).getSkill();
            enemySkill.setCooldown(enemySkill.getCooldown() - 1);
        }
    }

    /**
     * Sets both units skill cooldown to 3 turns.
     */
    private void setCooldown() {
        hero.getSkill().setCooldown(3);
        if (enemy.getClass() == SkilledUnit.class) hero.getSkill().setCooldown(3);
    }

    /**
     * Receives the input of the user's action during the battle. [A] for dealing
     * a normal attack and [S] for using a skill. Skill will not be used if the
     * player's skill is on cooldown.
     */
    public void battleIO() {
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
                    SkillHelper.useSkill(hero, enemy);
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

    /**
     * Prints both units current health and max health
     */
    private void printHealth() {
        System.out.println(hero.getName() + " \t\t\t\t\t"+ enemy.getName());
        System.out.print(hero.getCurrentHealth() + "/" + hero.getMaxHealth());
        System.out.print("\t\t\t\t\t" + enemy.getCurrentHealth() + "/" + enemy.getMaxHealth());
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
        //Will return if the attacker can evade the attack
        if (canEvade(victim.getUnitStats())) {
            System.out.println(victim.getName() + " evaded " + attacker.getName() + "'s attack");
            return;
        }


        System.out.println(attacker.getName() + " used normal attack");
        int damage = DamageHelper.damageOutput(attacker.getMinDamage(), attacker.getDamage());
        //Sets the damage to 2x the damage dealth if the attacker can crit
        if (canCrit(attacker.getUnitStats())) {
            damage += damage;
        }

        victim.takeDamage(damage);

        System.out.println(damage + " damage");
    }

    /**
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
                SkillHelper.useSkill((SkilledUnit) enemy, hero);
            } else {
                normalAttack(enemy, hero);
            }
            return;
        }
        normalAttack(enemy,hero);
    }
}
