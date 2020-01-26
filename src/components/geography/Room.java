package components.geography;

import misc.Direction;

import java.util.HashMap;
import java.util.Map;

public class Room {
    private String name;
    private Point point;
    private Map<Direction, Room> adjacentRoom = new HashMap<>();

    public Room(String name, Point point) {
        this.point = point;
        this.name  = name;
    }

    public Map<Direction, Room> getAdjacentRoom() {
        return adjacentRoom;
    }

    public Point getPoint() {
        return point;
    }

    public String getName() {
        return name;
    }

    /**
     * Sets the room for the direction given.
     * @param to
     * @param room
     */
    public void setAdjacentRoom(Direction to, Room room) {
        if (adjacentRoom.get(to) != null) {
            return;
        }

        adjacentRoom.put(to, room);
        room.setAdjacentRoom(to.getOpposite(), this);
    }
}
