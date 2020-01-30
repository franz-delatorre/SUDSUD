package components.geography;

import misc.Direction;

public interface CheckAdjacentRoom {
    boolean canMoveToAdjacentRoom(Direction to, Room rm);
}
