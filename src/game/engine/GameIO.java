package game.engine;

import java.util.Scanner;

public class GameIO {
    private static final Scanner scanner = new Scanner(System.in);

    public static String getMainOption() {
        System.out.println("[I] inventory");
        System.out.println("[M] map");
        System.out.println("[Q] quit");
        String opt = scanner.nextLine();
        return opt.toLowerCase();
    }
}
