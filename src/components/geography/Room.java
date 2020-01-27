package components.geography;

import components.unit.Unit;
import misc.Direction;

import java.util.HashMap;
import java.util.Map;

public class Room{
    private String name;
    private Point point;
    private Unit enemy;
    private Map<Direction, Room> adjacentRoom = new HashMap<>();

    public Room(String name, Point point) {
        this.point = point;
        this.name = name;
    }

    public void setEnemy(Unit enemy) {
        this.enemy = enemy;
    }

    public Unit getEnemy() {
        return enemy;
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

    public Room getAdjacentRoom(Direction to) {
        return adjacentRoom.get(to);
    }

    public boolean adjacentRoomExist(Direction to) {
        if (adjacentRoom.get(to) == null) return false;
        return true;
    }

    public boolean enemyIsAlive() {
        return enemy.isAlive();
    }

    public Room clone(){
        return new Room(name, point);
    }
}
