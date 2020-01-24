package game.engine;

import components.geography.GameMap;
import components.geography.Point;
import components.geography.Room;
import components.item.Inventory;
import components.item.Item;
import components.unit.Unit;
import dialogue.Dialogue;
import helper.MapHelper;

import java.util.ArrayList;

public class GameManager {
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

    public void gameProgress() {
        switch (progress) {
            case 0:
                Dialogue.actOne();
                progress = 1;
                break;
            case 1:
                MapHelper.showMap(map);
                progress = 2;
                break;
            case 2:
                this.getUserAction();
                gameOver = true;
        }
    }

    private void getUserAction() {
        String opt = GameIO.getMainOption();
        switch (opt) {
            case "i":
                break;
            case "m":
                MapHelper.showMap(map);
                break;
        }
    }

    public boolean gameIsOver() {
        return gameOver;
    }
}
