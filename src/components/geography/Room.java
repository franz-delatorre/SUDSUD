package components.geography;

import components.item.EquippableItem;
import components.item.Item;
import components.unit.Unit;
import misc.Direction;

import java.util.HashMap;
import java.util.Map;

public class Room{
    private String name;
    private Point point;
    private Unit enemy;
    private EquippableItem item;
    private Map<Direction, Room> adjacentRoom = new HashMap<>();

    public Room(String name, Point point) {
        this.point = point;
        this.name = name;
    }

    public void setEnemy(Unit enemy) {
        this.enemy = enemy;
    }

    public EquippableItem getItem() {
        return item;
    }

    public void setItem(EquippableItem item) {
        this.item = item;
    }

    public Unit getEnemy() {
        return enemy;
    }

    public Map<Direction, Room> getAdjacentRoom() {
        return adjacentRoom;
    }

    public Room clone(){
        return new Room(name, point);
    }

    public Point getPoint() {
        return point;
    }

    public String getName() {
        return name;
    }

    public Room getAdjacentRoom(Direction to) {
        return adjacentRoom.get(to);
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
