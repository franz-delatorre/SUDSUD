package game.engine;

import components.geography.Room;

import java.util.ArrayList;

public class Progress {
    ArrayList<ArrayList<Room>> roomsOpened = new ArrayList<>();

    public Progress() {
    }

    public void setRoomsOpened(Room[] rooms) {
        ArrayList<Room> rm = new ArrayList<>();
        for (Room r : rooms) {
            rm.add(r);
        }
        roomsOpened.add(rm);
    }

    public ArrayList<Room> getOpenedRooms(int index) {
        return roomsOpened.get(index);
    }
}
