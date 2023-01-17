package byow.Core;

import java.util.*;

public class Tessellation {
    private int worldWidth;
    private int worldHeight;
    private Random RANDOM;
    private List<Position> existedPositions;
    private List<Room> roomList;
    private Map<Position, Room> wallsToRoom;
    private int randomDoorIndex;
    private Position door;
    private Position player;
    private int totalCoinNum;
    private List<Position> coins;
    private List<Position> allFloors;
    private List<Position> allWalls;
    private long nextSeed;

    public Tessellation(long seed) {
        worldWidth = 35;
        worldHeight = 20;

        RANDOM = new Random(seed);
        existedPositions = new ArrayList<>();
        int roomNumber = RANDOM.nextInt(30) + 30;
        roomList = new ArrayList<>();
        wallsToRoom = new HashMap<>();
        coins = new ArrayList<>();

        for (int i = 0; i < roomNumber; i++) {
            Room room = createRoom();
            roomList.add(room);
            existedPositions.addAll(room.getAllPositions());
            for (Position p: room.getWallWithoutCorner()) {
                wallsToRoom.put(p, room);
            }

            if (room.getMaxY() >= worldHeight - 2) {
                worldHeight = room.getMaxY() + 2;
            }
            if (room.getMaxX() >= worldWidth - 2) {
                worldWidth = room.getMaxX() + 2;
            }
        }
        allFloors = allFloors();
        allWalls = allWalls();

        randomDoor(roomNumber);
        randomPlayer(roomNumber);
        nextSeed = RANDOM.nextLong();

        totalCoinNum = RANDOM.nextInt(10) + 5;
        while (coins.size() < totalCoinNum) {
            Position nextCoin = allFloors.get(RANDOM.nextInt(allFloors.size()));
            if (!nextCoin.equals(player) && !coins.contains(nextCoin)) {
                coins.add(nextCoin);
            }
        }
    }

    private void randomDoor(int roomNumber) {
        randomDoorIndex = RANDOM.nextInt(roomNumber);
        Room randomRoom = roomList.get(randomDoorIndex);
        List<Position> roomWalls = randomRoom.getBreakableWalls();
        if (roomWalls.isEmpty()) {
            randomDoor(roomNumber);
        } else {
            door = roomWalls.get(RANDOM.nextInt(roomWalls.size()));
            return;
        }
    }

    private void randomPlayer(int roomNumber) {
        int randomPlayerIndex = RANDOM.nextInt(roomNumber);
        while (randomPlayerIndex == randomDoorIndex) {
            randomPlayerIndex = RANDOM.nextInt(roomNumber);
        }
        Room playerRoom = roomList.get(randomPlayerIndex);
        List<Position> playerRoomFloors = playerRoom.getFloorPositions();
        player = playerRoomFloors.get(RANDOM.nextInt(playerRoomFloors.size()));
    }

    private List<Position> allFloors() {
        List<Position> results = new ArrayList<>();
        for (Room m : roomList) {
            results.addAll(m.getFloorPositions());
        }
        return results;
    }

    private List<Position> allWalls() {
        List<Position> results = new ArrayList<>();
        for (Room m : roomList) {
            results.addAll(m.getWallsPositions());
        }
        return results;
    }

    /** OpenSpace */
    private boolean getOpenSpace(List<Position> positions) {
        for (Position p : positions) {
            if (existedPositions.contains(p)) {
                return false;
            }
        }
        return true;
    }

    /** create a room or a hallway */
    private Room createRoom() {
        int roomX = RANDOM.nextInt(worldWidth) + 1;
        int roomY = RANDOM.nextInt(worldHeight) + 1;
        int roomWidth = RANDOM.nextInt(7) + 1;
        int roomHeight = RANDOM.nextInt(7) + 1;

        Position p = new Position(roomX, roomY);
        int number = RANDOM.nextInt(5);
        Room newRoom = new Room(p, roomWidth, roomHeight);

        if (number == 0) {
            newRoom = new HorizontalHallway(p, roomHeight);
        } else if (number == 1) {
            newRoom = new VerticalHallway(p, roomWidth);
        }

        List<Position> newPositions = newRoom.getAllPositions();

        if (wallsToRoom.isEmpty() || (getOpenSpace(newPositions) && touchCheck(newRoom))) {
            return newRoom;
        } else {
            return createRoom();
        }
    }



    private boolean touchCheck(Room r) {
        List<Position> touchCheckList = r.getTouchHelperPositions();
        if (r.getMaxY() + 2 >= 47) {
            return false;
        }
        for (Position p : touchCheckList) {
            if (wallsToRoom.containsKey(p)) {
                Position actualPosition = r.touchToWall().get(p);
                r.addNewExit(actualPosition);
                Room otherRoom = wallsToRoom.get(p);
                otherRoom.addNewExit(p);
                return true;
            }
        }
        return false;
    }

    public List<Room> getRoomList() {
        return roomList;
    }

    public int getWorldHeight() {
        return worldHeight;
    }

    public int getWorldWidth() {
        return worldWidth;
    }

    public Position getDoor() {
        return door;
    }

    public Position getPlayer() {
        return player;
    }

    public List<Position> getCoins() {
        return coins;
    }

    public List<Position> getAllFloors() {
        return allFloors;
    }

    public List<Position> getAllWalls() {
        return allWalls;
    }

    public long getNextSeed() {
        return nextSeed;
    }

    public int getTotalCoinNum() {
        return totalCoinNum;
    }
}
