package components.geography;

import misc.Direction;
import misc.TextColor;

import java.util.ArrayList;

public class GameMap implements ShowMap, CheckAdjacentRoom, ReturnBossRoom {
    private ArrayList<Room> openRooms;
    private Room heroLocation;

    public GameMap(ArrayList<Room> openRooms, Room heroLocation) {
        this.openRooms = openRooms;
        this.heroLocation = heroLocation;
    }


    public void setHeroLocation(Room heroLocation) {
        this.heroLocation = heroLocation;
    }

    public void setGameMap(ArrayList<Room> newRoom) {
        openRooms = newRoom;
    }

    public Room getHeroLocation() {
        return heroLocation;
    }

    /**
     * Boss room should be the last room added to the openRooms. This limit should be done during
     * the construction of the rooms and setting the game map using the 
     * {@link GameMap#setGameMap(ArrayList) method.
     * @return
     */
    public Room getBossRoom() {
        return openRooms.get(openRooms.size() - 1);
    }

    /**
     * Will return true if the adjacent direction's room is in the list of
     * opened rooms in the game map.
     * @param to
     * @param room
     * @return
     */
    public boolean canMoveToAdjacentRoom (Direction to, Room room) {
        Room adjRm = room.getAdjacentRoom(to);
        if (openRooms.contains(adjRm)) return true;
        return false;
    }

    /**
     * Shows the map of the available rooms based on the user's progress. Will also show
     * the current location of the player's character.
     */
    public void showMap() {

        // Index 0 for min range and index 1 for max range
        int[] xRange = {0, 0};
        int[] yRange = {0, 0};

        //Sets the x and y axis range of the map.
        for (int it = 0; it < openRooms.size(); it++) {
            Point rmPoint = openRooms.get(it).getPoint();
            xRange = getAxisRange(xRange, rmPoint.getxAxis());
            yRange = getAxisRange(yRange, rmPoint.getyAxis());
        }

        System.out.println(TextColor.ANSI_RESET +  "=================================" + TextColor.ANSI_BLACK);
        for (int y = yRange[1]; y >= yRange[0]; y--) {
            for (int x = xRange[0]; x <= xRange[1]; x++) {

                //Checks if the room exist at the specified point
                if (roomExist(new Point(x, y))) {
                    Point heroPoints = heroLocation.getPoint();

                    //Checks if the hero is at the current specified point
                    if (heroPoints.getyAxis() == y && heroPoints.getxAxis() == x) {
                        System.out.printf(TextColor.ANSI_GREEN + "[ * ]" + TextColor.ANSI_BLACK);
                    } else {
                        System.out.printf("[   ]");
                    }
                } else {
                    System.out.printf("\t  ");
                }
            }
            System.out.println("\n");
        }
        System.out.println("      == "+ TextColor.ANSI_GREEN + heroLocation.getName() + TextColor.ANSI_BLACK + " ==      ");
        System.out.println(TextColor.ANSI_RESET +  "=================================" + TextColor.ANSI_BLACK);
    }

    /**
     * Gets the min and max range of each the axis. Index 0 is the min and index 1 is the
     * max range.
     * @param range
     * @param axis
     * @return
     */
    private int[] getAxisRange(int[] range, int axis) {
        int min = range[0];
        int max = range[1];
        if (axis < range[0]) min = axis;
        if (axis > range[1]) max = axis;
        return new int[] {min, max};
    }

    /**
     * Checks if the point exist in the list of rooms.
     * @param pt
     * @return
     */
    private boolean roomExist(Point pt) {
        for (int it = 0; it < openRooms.size(); it++) {
            Point o = openRooms.get(it).getPoint();
            if (o.getxAxis() == pt.getxAxis() && o.getyAxis() == pt.getyAxis()) {
                return true;
            }
        }
        return false;
    }
}
