package misc;

import components.Stats;

public class Broadcaster {
    private static final String BLACK = TextColor.ANSI_BLACK;
    private static final String CYAN = TextColor.ANSI_CYAN;
    private static final String RED = TextColor.ANSI_RED;
    private static final String PURPLE = TextColor.ANSI_PURPLE;
    private static final String BLUE = TextColor.ANSI_BLUE;
    private static final String GREEN = TextColor.ANSI_GREEN;

    public static void relayDamage(int damage) {
        System.out.println(CYAN + damage + " damage" + BLACK);
    }

    public static void relayHeal(int heal) {
        System.out.println(GREEN + heal + " heal" + BLACK);
    }

    public static void relayLifesteal(int ls) {
        System.out.println(GREEN + ls + " health is replenished" + BLACK);
    }

    public static void relayBuff(Stats stats) {
        for (StatType statType: StatType.values()) {
            if (stats.getStatValue(statType) > 0) {
                System.out.println(GREEN + "Increase " + statType.toString() + " by " + stats.getStatValue(statType) + BLACK);
            }
        }
    }

    public static void relayDebuff(Stats stats) {
        for (StatType statType: StatType.values()) {
            if (stats.getStatValue(statType) > 0) {
                System.out.println(RED + "Decreased " + statType.toString() + " by " + stats.getStatValue(statType) + BLACK);
            }
        }
    }

    public static void relayGameOver() {
        System.out.println(RED + "GAME OVER!!!" + BLACK);
    }

    public static void relayMissionAccomplished() {
        System.out.println(GREEN + "Congratulations!! You win the game!!");
    }
}
