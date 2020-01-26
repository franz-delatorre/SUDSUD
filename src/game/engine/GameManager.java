package game.engine;

import components.geography.GameMap;
import components.geography.Room;
import components.item.Inventory;
import components.item.Item;
import components.unit.Unit;
import dialogue.Dialogue;
import util.MapHelper;

import java.util.ArrayList;
import java.util.Scanner;

public class GameManager {

    private static final Scanner scanner = new Scanner(System.in);

    private ArrayList<Item> gameInventory;
    private ArrayList<Unit> units;
    private ArrayList<Room> rooms;
    private Inventory alucardInventory;
    private int tier;
    private int progress;
    private GameMap map;
    private boolean gameOver;

    public GameManager() {
        GameInitializer gi = new GameInitializer();
        gi.setupRooms();
        gi.setupItems();
        gi.getUnits();

        gameOver         = false;
        progress         = 0;
        tier             = 1;
        gameInventory    = gi.getItems();
        units            = gi.getUnits();
        rooms            = gi.getRooms();
        alucardInventory = new Inventory();
        map              = gi.getGameMap();
    }

    public void initialize() {
        Dialogue.actOne();
    }

    public void getUserAction() {

        System.out.println("[I] inventory");
        System.out.println("[M] map");
        System.out.println("[Q] quit");
        String opt = scanner.nextLine().toLowerCase();

        switch (opt) {
            case "i":
                break;
            case "m":
                map.showMap();
                move();
                break;
            default:
                System.out.println("Action invalid.");
                getUserAction();
        }
    }

    private void move() {
        System.out.println("[e] Exit map");



        System.out.println("[W] Move up");
        System.out.println("[A] Move left");
        System.out.println("[S] Move down");
        System.out.println("[A] Move right");
    }

    public boolean gameIsOver() {
        return gameOver;
    }

}
