package byow.Core;

import java.util.*;


public class Room {
    private Position bottomLeft;
    private Position topRight;
    private int width;
    private int height;
    private List<Position> floors;
    private List<Position> walls;
    private List<Position> wallWithoutCorner;
    private List<Position> breakableWalls;
    private List<Position> exits;
    private Map<Position, Position> touchHelperPositions;


    public Room(Position p, int width, int height) {
        this.bottomLeft = p;
        this.topRight = new Position(p.getX() + width - 1, p.getY() + height - 1);
        this.width = width;
        this.height = height;

        addFloorPositions();
        addWallPositions();
        breakableWalls = wallWithoutCorner;
        exits = new ArrayList<>();
    }

    /** Add the all positions of floor at initial condition. */
    private void addFloorPositions() {
        floors = new ArrayList<>();
        for (int x = bottomLeft.getX(); x <= topRight.getX(); x++) {
            for (int y = bottomLeft.getY(); y <= topRight.getY(); y++) {
                floors.add(new Position(x, y));
            }
        }
    }

    /** Add the all positions of wall at initial condition (closed room):
     *  There is a whole round of walls around the floors, including the four extra conor. */
    private void addWallPositions() {
        walls = new ArrayList<>();
        wallWithoutCorner = new ArrayList<>();
        touchHelperPositions = new HashMap<>();
        for (int x = bottomLeft.getX(); x <= topRight.getX(); x++) {
            Position p1 = new Position(x, bottomLeft.getY() - 1);
            walls.add(p1);
            wallWithoutCorner.add(p1);
            touchHelperPositions.put(new Position(x, bottomLeft.getY() - 2), p1);

            Position p2 = new Position(x, topRight.getY() + 1);
            walls.add(p2);
            wallWithoutCorner.add(p2);
            touchHelperPositions.put(new Position(x, topRight.getY() + 2), p2);
        }

        for (int y = bottomLeft.getY(); y <= topRight.getY(); y++) {
            Position p3 = new Position(bottomLeft.getX() - 1, y);
            walls.add(p3);
            wallWithoutCorner.add(p3);
            touchHelperPositions.put(new Position(bottomLeft.getX() - 2, y), p3);

            Position p4 = new Position(topRight.getX() + 1, y);
            walls.add(p4);
            wallWithoutCorner.add(p4);
            touchHelperPositions.put(new Position(topRight.getX() + 2, y), p4);
        }

        walls.add(new Position(bottomLeft.getX() - 1, bottomLeft.getY() - 1));
        walls.add(new Position(bottomLeft.getX() - 1, topRight.getY() + 1));
        walls.add(new Position(topRight.getX() + 1, topRight.getY() + 1));
        walls.add(new Position(topRight.getX() + 1, bottomLeft.getY() - 1));
    }

    /** To make the rooms and hallways connected, there must be some exits on the walls.
     * When a new exit is created, this position should turn into floor from wall.
     * Each exit only in width of one, and there must be walls to separate each exit.
     * Which means the walls next to an exit cannot be broken or become a new exit. */
    public void addNewExit(Position e) {
        if (!breakableWalls.contains(e)) {
            throw new NoSuchElementException(
                    "No wall at this position, or this wall cannot be broken.");
        } else {
            makeNeighborsUnbreakable(e);
            breakableWalls.remove(e);
            walls.remove(e);
            floors.add(e);
            exits.add(e);
        }
    }

    private List<Position> getNeighbors(Position e) {
        List<Position> neighbors = new ArrayList<>();
        neighbors.add(new Position(e.getX() - 1, e.getY()));
        neighbors.add(new Position(e.getX() + 1, e.getY()));
        neighbors.add(new Position(e.getX(), e.getY() - 1));
        neighbors.add(new Position(e.getX(), e.getY() + 1));
        return neighbors;
    }

    private void makeNeighborsUnbreakable(Position e) {
        List<Position> neighbors = getNeighbors(e);
        for (Position p : neighbors) {
            if (breakableWalls.contains(p)) {
                breakableWalls.remove(p);
            }
        }
    }



    public Position getPosition() {
        return bottomLeft;
    }

    public int getWidth() {
        return width;
    }

    public int getLength() {
        return height;
    }

    public List<Position> getFloorPositions() {
        return floors;
    }

    public List<Position> getWallsPositions() {
        return walls;
    }

    public List<Position> getWallWithoutCorner() {
        return wallWithoutCorner;
    }

    public List<Position> getBreakableWalls() {
        return breakableWalls;
    }

    public Map<Position, Position> touchToWall() {
        return touchHelperPositions;
    }

    public List<Position> getTouchHelperPositions() {
        List<Position> helperPositions = new ArrayList<>();
        helperPositions.addAll(touchHelperPositions.keySet());
        return helperPositions;
    }

    public List<Position> getAllPositions() {
        List<Position> allPositions = new ArrayList<>();
        allPositions.addAll(getFloorPositions());
        allPositions.addAll(getWallsPositions());
        return allPositions;
    }

    public int getMaxX() {
        return topRight.getX();
    }

    public int getMaxY() {
        return topRight.getY();
    }
}
