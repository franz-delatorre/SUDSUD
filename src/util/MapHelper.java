package util;

import components.geography.GameMap;
import components.geography.Point;
import components.geography.Room;
import misc.Direction;

import java.util.ArrayList;
import java.util.Map;

public final class MapHelper {

    public static boolean checkAdjacentRoom(Direction to, Room room) {
        Map<Direction, Room> adjacentRoom = room.getAdjacentRoom();
        if (adjacentRoom.get(to) != null) {
            return true;
        }
        return false;
    }


}
