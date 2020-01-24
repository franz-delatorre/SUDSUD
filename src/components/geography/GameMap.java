package components.geography;

import components.geography.Room;

import java.util.ArrayList;

public class GameMap {
    private ArrayList<Room> openRooms;
    private Point heroLocation;

    public GameMap(ArrayList<Room> openRooms, Point heroLocation) {
        this.openRooms = openRooms;
        this.heroLocation = heroLocation;
    }

    public void setHeroLocation(Point heroLocation) {
        this.heroLocation = heroLocation;
    }

    public void addOpenRoom(Room room) {
        this.openRooms.add(room);
    }

    public ArrayList<Room> getOpenRooms() {
        return openRooms;
    }

    public Point getHeroLocation() {
        return heroLocation;
    }

    /**
     * Checks if the point exist in the list of rooms.
     * @param pt
     * @return
     */
    public boolean roomExist(Point pt) {
        for (int it = 0; it < openRooms.size(); it++) {
            Point o = openRooms.get(it).getPoint();
            if (o.getxAxis() == pt.getxAxis() && o.getyAxis() == pt.getyAxis()) {
                return true;
            }
        }
        return false;
    }
}
