package game.engine;

import components.geography.Room;

import java.util.ArrayList;

public class GameMapProgress {
    ArrayList<ArrayList<Room>> roomsOpened = new ArrayList<>();

    public GameMapProgress() {
    }

    public void addRoomsOpened(ArrayList<Room> rooms) {
        roomsOpened.add(rooms);
    }

    public ArrayList<Room> getOpenedRooms(int index) {
        return roomsOpened.get(index);
    }

    public int size() {
        return roomsOpened.size();
    }
}
