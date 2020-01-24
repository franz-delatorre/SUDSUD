package components.geography;

import java.util.ArrayList;

public class GameMap {
    private ArrayList<Room> rooms;
    private Point heroLocation;

    public GameMap(ArrayList<Room> openRooms, Point heroLocation) {
        this.rooms = openRooms;
        this.heroLocation = heroLocation;
    }

    /**
     * Checks if the point exist in the list of rooms.
     * @param pt
     * @return
     */
    private boolean roomExist(Point pt) {
        for (int it = 0; it < rooms.size(); it++) {
            Point o = rooms.get(it).getPoint();
            if (o.getxAxis() == pt.getxAxis() && o.getyAxis() == pt.getyAxis()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Shows the rooms of the available rooms based on the user's progress. Will also show
     * the current location of the player's character.
     */
    public void showrooms() {
        Point heroLoc      = heroLocation;
        ArrayList<Room> rm = rooms;

        // Index 0 for min range and index 1 for max range
        int[] xRange = {0, 0};
        int[] yRange = {0, 0};

        //Sets the x and y axis range of the rooms.
        for (int it = 0; it < rm.size(); it++) {
            Point rmPoint = rm.get(it).getPoint();
            xRange        = getAxisRange(xRange, rmPoint.getxAxis());
            yRange        = getAxisRange(yRange, rmPoint.getyAxis());
        }

        System.out.println("=================================");
        for (int y = yRange[1]; y >= yRange[0]; y--) {
            for (int x = xRange[0]; x <= xRange[1]; x++) {

                //Checks if the room exist at the specified point
                if (this.roomExist(new Point(x, y))) {

                    //Checks if the hero is at the current specified point
                    if (heroLoc.getyAxis() == y && heroLoc.getxAxis() == x) {
                        System.out.printf("[ * ]");
                    } else {
                        System.out.printf("[   ]");
                    }
                } else {
                    System.out.printf("\t  ");
                }
            }
            System.out.println("\n");
        }
        System.out.println("=================================");
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
}
