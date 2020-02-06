package components.geography;

import components.item.EquippableItem;
import components.unit.Unit;
import components.unit.UnskilledUnit;
import misc.Direction;

import java.util.EnumMap;

public class Room{
    private EquippableItem item;
    private String name;
    private EnumMap<Direction, Room> adjacentRoom = new EnumMap<>(Direction.class);
    private Point point;
    private Unit enemy;

    public Room(String name, Point point) {
        this.point = point;
        this.name = name;
        enemy = new UnskilledUnit.Builder().health(0).build();
        item = new EquippableItem.Builder().build();
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
