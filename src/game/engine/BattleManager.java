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

    private void decrementCooldown() {
        Skill heroSkill = hero.getSkill();
        heroSkill.setCooldown(heroSkill.getCooldown() - 1);
        if (enemy.getClass() == SkilledUnit.class) {
            Skill enemySkill = ((SkilledUnit) enemy).getSkill();
            enemySkill.setCooldown(enemySkill.getCooldown() - 1);
        }
    }

    private void setCooldown() {
        hero.getSkill().setCooldown(3);
        if (enemy.getClass() == SkilledUnit.class) hero.getSkill().setCooldown(3);
    }

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

    private void printHealth() {
        System.out.println(hero.getName() + " \t\t\t\t\t"+ enemy.getName());
        System.out.print(hero.getCurrentHealth() + "/" + hero.getMaxHealth());
        System.out.print("\t\t\t\t\t" + enemy.getCurrentHealth() + "/" + enemy.getMaxHealth());
    }

    private void setHealth() {
        hero.getHealth().setCurrentHealth(hero.getMaxHealth());
        enemy.getHealth().setCurrentHealth(enemy.getHealth().getMaxHealth());
    }

    private boolean fightersStillAlive() {
        int heroCurrHealth = hero.getCurrentHealth();
        int enemyCurrHealth = enemy.getCurrentHealth();
        if (heroCurrHealth > 0 && enemyCurrHealth > 0) return true;
        return false;
    }

    private int winner() {
        if (enemy.getCurrentHealth() <= 0 ) {
            enemy.setIsAlive(false);
            return 1;
        }
        return 0;
    }

    private void normalAttack(Unit attacker, Unit victim) {
        if (canEvade(victim.getUnitStats())) {
            System.out.println(victim.getName() + " evaded " + attacker.getName() + "'s attack");
            return;
        }

        System.out.println(attacker.getName() + " used normal attack");
        int damage = DamageHelper.damageOutput(attacker.getDamage());
        if (canCrit(attacker.getUnitStats())) {
            damage += damage;
        }

        victim.takeDamage(damage);

        System.out.println(damage + " damage");
    }

    private boolean canCrit(Stats stats) {
        if (RandomGenerator.getRandomInt(100) <= stats.getStatValue(StatType.CRITICAL_CHANCE)) return true;
        return false;
    }

    private boolean canEvade(Stats stats) {
        if (RandomGenerator.getRandomInt(100) <= stats.getStatValue(StatType.EVASION)) return true;
        return false;
    }

    private void enemyAI() {
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
