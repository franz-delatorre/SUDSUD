package misc;

import components.Stats;

import static misc.TextColor.*;

public class Broadcaster {
    

    public static void relayDamage(int damage) {
        System.out.println(ANSI_CYAN + damage + " damage" + ANSI_BLACK);
    }

    public static void relayHeal(int heal) {
        System.out.println(ANSI_GREEN + heal + " heal" + ANSI_BLACK);
    }

    public static void relayLifesteal(int ls) {
        System.out.println(ANSI_GREEN + ls + " health is replenished" + ANSI_BLACK);
    }

    public static void relayBuff(Stats stats) {
        for (StatType statType: StatType.values()) {
            if (stats.getStatValue(statType) > 0) {
                System.out.println(ANSI_GREEN + "Increase " + statType.toString() + " by " + stats.getStatValue(statType) + ANSI_BLACK);
            }
        }
    }

    public static void relayDebuff(Stats stats) {
        for (StatType statType: StatType.values()) {
            if (stats.getStatValue(statType) > 0) {
                System.out.println(ANSI_RED + "Decreased " + statType.toString() + " by " + stats.getStatValue(statType) + ANSI_BLACK);
            }
        }
    }

    public static void relayGameOver() {
        System.out.println(ANSI_RED + "GAME OVER!!!" + ANSI_BLACK);
    }

    public static void relayMissionAccomplished() {
        System.out.println(ANSI_GREEN + "Congratulations!! You win the game!!");
    }
}
