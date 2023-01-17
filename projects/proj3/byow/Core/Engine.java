package byow.Core;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;
import edu.princeton.cs.introcs.StdDraw;

import java.awt.*;
import java.io.*;
import java.util.*;
import java.util.List;


public class Engine {
    TERenderer ter = new TERenderer();
    public static final int WIDTH = 50;
    public static final int HEIGHT = 50;
    private long SEED;
    private long nextSeed;
    private TETile[][] currentWorld;
    private final List<Character> numbers =
            Arrays.asList('0', '1', '2', '3', '4', '5', '6', '7', '8', '9');
    private final List<Character> KEY = Arrays.asList('w', 'a', 's', 'd');
    private final List<Character> mode = Arrays.asList('1', '2', '3', '4');

    private boolean inGame;
    private boolean levelCleared;
    private boolean paused;
    private boolean failed;
    private boolean about;

    private int totalCoinsNum;
    private int coinsGot;
    private Position door;
    private Position player;
    private boolean doorLocked;
    private int roundNum;
    private List<Position> coins;
    private int attemptLeft;

    private final String egg = "November 29th, 2020, this is the birthday of our game!";
    private double r; //easy:12  normal:8  hard:5

    public Engine() {
        currentWorld = null;
        inGame = false;
        levelCleared = false;
        paused = false;
        failed = false;
        about = false;
        coinsGot = 0;
        roundNum = 1;
        coins = new ArrayList<>();
        loadGame();
    }

    /**
     * Method used for exploring a fresh world. This method should handle all inputs,
     * including inputs from the main menu.
     */
    public void interactWithKeyboard() {
        drawMenu();
        while (true) {
            if (StdDraw.hasNextKeyTyped()) {
                char next = Character.toLowerCase(StdDraw.nextKeyTyped());
                if (next == ':') {
                    int k = 0;
                    while (k < 1) {
                        if (StdDraw.hasNextKeyTyped()) {
                            next = Character.toLowerCase(StdDraw.nextKeyTyped());
                            if (next == 'q') {
                                quitSaving();
                                System.exit(0);
                            }
                            k += 1;
                        }
                    }
                } else if (next == 'q') {
                    if (inGame && !paused && !levelCleared && !failed) {
                        continue;
                    } else {
                        drawMenu();
                    }
                } else if (!inGame && !about) {
                    if (next == 'n') {
                        drawTypedString("");
                        TETile[][] world = afterTypedN();
                        if (world != null) {
                            drawNewGame(world);
                            currentWorld = world;
                        }
                    } else if (next == 'l') {
                        drawNewGame(currentWorld);
                        if (levelCleared) {
                            drawLevelCleared();
                        } else if (failed) {
                            drawFail();
                        }
                    } else if (next == 'a') {
                        about = true;
                        drawAbout();
                    }
                } else if (inGame) {
                    if (!levelCleared && !paused && !failed) {
                        if (KEY.contains(next)) {
                            movePlayer(next);
                            redraw(currentWorld);
                            if (attemptLeft == 0) {
                                drawFail();
                                failed = true;
                            }
                            if (levelCleared) {
                                drawLevelCleared();
                            }
                        } else if (next == 'u') {
                            drawPause();
                            paused = true;
                        }
                    } else {
                        if (next == 'p') {
                            currentWorld = createNewGame(SEED);
                            redraw(currentWorld);
                            paused = false;
                            levelCleared = false;
                        } else if (paused) {
                            if (next == 'r') {
                                redraw(currentWorld);
                                paused = false;
                            }
                        } else if (levelCleared) {
                            if (next == 'x') {
                                SEED = nextSeed;
                                currentWorld = createNewGame(SEED);
                                roundNum += 1;
                                drawNewGame(currentWorld);
                            }
                        }
                    }
                }
            } else if (StdDraw.isMousePressed() && inGame && !levelCleared && !paused) {
                mouseControl();
            }
        }
    }

    private void quitSaving() {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("Savingfile.txt"));
            writer.write("" + SEED);
            writer.newLine();
            writer.write("" + player.getX());
            writer.newLine();
            writer.write("" + player.getY());
            writer.newLine();
            writer.write("" + roundNum);
            writer.newLine();
            writer.write("" + inGame);
            writer.newLine();
            writer.write("" + levelCleared);
            writer.newLine();
            writer.write("" + failed);
            writer.newLine();
            writer.write("" + doorLocked);
            writer.newLine();
            writer.write("" + totalCoinsNum);
            writer.newLine();
            writer.write("" + coinsGot);
            writer.newLine();
            writer.write("" + attemptLeft);
            writer.newLine();
            writer.write("" + r);
            writer.newLine();

            for (Position c : coins) {
                writer.write("" + c.getX());
                writer.newLine();
                writer.write("" + c.getY());
                writer.newLine();
            }

            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void mouseControlMsg(TETile tile) {
        StdDraw.clear();
        redraw(currentWorld);
        if (tile == Tileset.WALL) {
            StdDraw.text(currentWorld.length / 2, currentWorld[0].length + 2, "Wall: can’t go through");
        } else if (tile == Tileset.FLOOR) {
            StdDraw.text(currentWorld.length / 2, currentWorld[0].length + 2, "Floor: you can step on");
        } else if (tile == Tileset.LOCKED_DOOR) {
            StdDraw.text(currentWorld.length / 2, currentWorld[0].length + 2, "LockedDoor: the gate is closed");
        } else if (tile == Tileset.UNLOCKED_DOOR) {
            StdDraw.text(currentWorld.length / 2, currentWorld[0].length + 2, "UnlockedDoor: the gate is open");
        } else if (tile == Tileset.COIN) {
            StdDraw.text(currentWorld.length / 2, currentWorld[0].length + 2, "Coin: you can earn point by collecting it");
        } else if (tile == Tileset.AVATAR) {
            StdDraw.text(currentWorld.length / 2, currentWorld[0].length + 2, "Player: Yourself");
        }
        StdDraw.show();
    }

    private void mouseControl() {
        if (StdDraw.mouseY() < currentWorld[0].length) {
            int x = (int) StdDraw.mouseX();
            int y = (int) StdDraw.mouseY();
            TETile pressedTile = currentWorld[x][y];
            if ((r < 0) || (r > 0 && Math.pow(player.getX() - x, 2)
                    + Math.pow(player.getY() - y, 2) <= r * r)) {
                if (pressedTile != Tileset.NOTHING) {
                    StdDraw.setPenColor(StdDraw.WHITE);
                    mouseControlMsg(pressedTile);
                   return;
                }
            }
        }
        redraw(currentWorld);

    }

    private TETile[][] afterTypedN() {
        String s = "";
        while (true) {
            if (StdDraw.hasNextKeyTyped()) {
                char next = StdDraw.nextKeyTyped();
                if (numbers.contains(next)) {
                    s += next;
                    drawTypedString(s);
                } else if (next == 's') {
                    if (s.length() != 0) {
                        drawChooseMode();
                        while (true) {
                            if (StdDraw.hasNextKeyTyped()) {
                                char next1 = StdDraw.nextKeyTyped();
                                if (mode.contains(next1)) {
                                    chooseMode(next1);
                                    break;
                                } else {
                                    drawMenu();
                                    return null;
                                }
                            }
                        }
                        break;
                    }
                } else {
                    drawMenu();
                    return null;
                }
            }
        }

        roundNum = 1;
        SEED = Long.parseLong(s);
        return createNewGame(SEED);
    }

    private void movePlayer(char direction) {
        int newX = player.getX();
        int newY = player.getY();
        if (direction == 'w') {
            newY += 1;
        } else if (direction == 'a') {
            newX -= 1;
        } else if (direction == 's') {
            newY -= 1;
        } else if (direction == 'd') {
            newX += 1;
        }
        TETile nearTile = currentWorld[newX][newY];
        if (!nearTile.equals(Tileset.WALL)) {
            if (nearTile.equals(Tileset.LOCKED_DOOR)) {
                if (coinsGot < totalCoinsNum) {
                    attemptLeft -= 1;
                } else {
                    currentWorld[newX][newY] = Tileset.UNLOCKED_DOOR;
                    doorLocked = false;
                }
                return;
            } else if (nearTile.equals(Tileset.UNLOCKED_DOOR)) {
                currentWorld[newX][newY] = Tileset.AVATAR;
                currentWorld[player.getX()][player.getY()] = Tileset.FLOOR;
                levelCleared = true;
            } else {
                if (nearTile.equals(Tileset.COIN)) {
                    coinsGot += 1;
                    coins.remove(new Position(newX, newY));
                }
                currentWorld[newX][newY] = Tileset.AVATAR;
                currentWorld[player.getX()][player.getY()] = Tileset.FLOOR;
                player = new Position(newX, newY);
            }
        }
    }

    private TETile[][] createNewGame(long seed) {
        coinsGot = 0;
        inGame = true;
        levelCleared = false;
        paused = false;
        failed = false;
        doorLocked = true;
        attemptLeft = 2;

        if (Long.compare(seed, 9223372036854775807L) > 0) {
            throw new RuntimeException("Seed is too large.");
        }
        Tessellation tessellation = new Tessellation(seed);

        int width = tessellation.getWorldWidth();
        int height = tessellation.getWorldHeight();
        nextSeed = tessellation.getNextSeed();
        totalCoinsNum = tessellation.getTotalCoinNum();

        //ter.initialize(width, height);
        TETile[][] world = new TETile[width][height];

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                world[x][y] = Tileset.NOTHING;
            }
        }

        for (Position f : tessellation.getAllFloors()) {
            world[f.getX()][f.getY()] = Tileset.FLOOR;
        }

        for (Position w : tessellation.getAllWalls()) {
            world[w.getX()][w.getY()] = Tileset.WALL;
        }

        door = tessellation.getDoor();
        world[door.getX()][door.getY()] = Tileset.LOCKED_DOOR;

        player = tessellation.getPlayer();
        world[player.getX()][player.getY()] = Tileset.AVATAR;

        coins = tessellation.getCoins();
        for (Position c : coins) {
            world[c.getX()][c.getY()] = Tileset.COIN;
        }

        return world;
    }

    private void drawMenu() {
        inGame = false;
        about = false;
        StdDraw.setCanvasSize(WIDTH * 16, HEIGHT * 16);
        StdDraw.setXscale(0, WIDTH);
        StdDraw.setYscale(0, HEIGHT);
        StdDraw.clear(StdDraw.BLACK);
        //ter.initialize(WIDTH, HEIGHT);
        StdDraw.setPenColor(StdDraw.WHITE);

        Font title = new Font("title", Font.ROMAN_BASELINE, 50);
        StdDraw.setFont(title);
        StdDraw.text(WIDTH / 2, 4 * HEIGHT / 5, "61B: THE GAME");

        Font menu = new Font("menu", Font.PLAIN, 25);
        StdDraw.setFont(menu);
        StdDraw.text(WIDTH / 2, HEIGHT / 2 + 2, "New Game (N)");
        StdDraw.text(WIDTH / 2, HEIGHT / 2, "Load Game (L)");
        StdDraw.text(WIDTH / 2, HEIGHT / 2 - 2, "Quit (Q)");
        StdDraw.text(WIDTH / 2, HEIGHT / 2 - 4, "Save snd Close (:Q)");
        StdDraw.text(WIDTH / 2, 2, "ABOUT (A)");

        StdDraw.show();
    }

    private void drawLevelCleared() {
        StdDraw.setPenColor(new Color(80, 110, 80));
        StdDraw.filledRectangle(currentWorld.length / 2, currentWorld[0].length / 2, 12, 8);

        StdDraw.setPenColor(StdDraw.WHITE);
        Font subtitle = new Font("Subtitle", Font.BOLD, 35);
        StdDraw.setFont(subtitle);
        StdDraw.text(currentWorld.length / 2, currentWorld[0].length / 2 + 4, "LEVEL CLEARED!");

        Font body = new Font("menu", Font.PLAIN, 25);
        StdDraw.setFont(body);
        StdDraw.text(currentWorld.length / 2, currentWorld[0].length / 2,
                "Coins " + coinsGot + "/" + totalCoinsNum);

        Font choice = new Font("menu", Font.PLAIN, 15);
        StdDraw.setFont(choice);
        StdDraw.text(currentWorld.length / 2, currentWorld[0].length / 2 - 3, "Play Again (P)");
        StdDraw.text(currentWorld.length / 2, currentWorld[0].length / 2 - 4, "Next (X)");
        StdDraw.text(currentWorld.length / 2, currentWorld[0].length / 2 - 5, "Quit (Q)");

        StdDraw.show();
    }

    private void drawNewGame(TETile[][] world) {
        if (world == null) {
            return;
        }
        ter.initialize(world.length, world[0].length + 6);
        //ter.renderFrame(world);
        ter.renderCircle(world, player, r);
        drawCaption(world);
        StdDraw.show();
        inGame = true;
    }

    private void drawCaption(TETile[][] world) {
        StdDraw.setPenColor(Color.WHITE);
        StdDraw.line(0, world[0].length + 1, world.length, world[0].length + 1);
        StdDraw.textLeft(0, world[0].length + 5, "ROUND: " + roundNum);
        StdDraw.textLeft(0, world[0].length + 3.5, "COINS: " + coinsGot);
        StdDraw.textRight(world.length, world[0].length + 3.5, "PAUSE(U)");
        StdDraw.text(world.length / 2, world[0].length + 5,
                "Collect all COINS to win!");
        StdDraw.text(world.length / 2, world[0].length + 3.5,
                "You have " + attemptLeft + " more chance to unlock the door.");

        String mode = "";
        if (r == 5) {
            mode = "Hard";
        } else if (r == 8) {
            mode = "Normal";
        } else if (r == 12) {
            mode = "Easy";
        } else if (r < 0) {
            mode = "Light Up";
        }
        StdDraw.textLeft(0, world[0].length + 2, "MODE: " + mode);
    }

    private void redraw(TETile[][] world) {
        //ter.renderFrame(world);
        ter.renderCircle(world, player, r);
        drawCaption(world);
        StdDraw.show();
    }

    private void loadGame() {
        File savingFile = new File("Savingfile.txt");
        if (savingFile == null || savingFile.length() == 0) {
            return;
        }

        try {
            BufferedReader reader = new BufferedReader(new FileReader(savingFile));

            SEED = Long.parseLong(reader.readLine());

            int playerX = Integer.parseInt(reader.readLine());
            int playerY = Integer.parseInt(reader.readLine());
            player = new Position(playerX, playerY);

            roundNum = Integer.parseInt(reader.readLine());
            inGame = Boolean.parseBoolean(reader.readLine());
            levelCleared = Boolean.parseBoolean(reader.readLine());
            failed = Boolean.parseBoolean(reader.readLine());
            doorLocked = Boolean.parseBoolean(reader.readLine());
            totalCoinsNum = Integer.parseInt(reader.readLine());
            coinsGot = Integer.parseInt(reader.readLine());
            attemptLeft = Integer.parseInt(reader.readLine());
            r = Double.parseDouble(reader.readLine());

            String nextLn = reader.readLine();
            while (nextLn != null && nextLn.length() != 0) {
                int coinX = Integer.parseInt(nextLn);
                int coinY = Integer.parseInt(reader.readLine());
                coins.add(new Position(coinX, coinY));
                nextLn = reader.readLine();
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Tessellation tessellation = new Tessellation(SEED);

        int width = tessellation.getWorldWidth();
        int height = tessellation.getWorldHeight();
        nextSeed = tessellation.getNextSeed();

        TETile[][] world = new TETile[width][height];

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                world[x][y] = Tileset.NOTHING;
            }
        }

        for (Position f : tessellation.getAllFloors()) {
            world[f.getX()][f.getY()] = Tileset.FLOOR;
        }

        for (Position w : tessellation.getAllWalls()) {
            world[w.getX()][w.getY()] = Tileset.WALL;
        }

        world[player.getX()][player.getY()] = Tileset.AVATAR;

        door = tessellation.getDoor();
        if (doorLocked) {
            world[door.getX()][door.getY()] = Tileset.LOCKED_DOOR;
        } else {
            world[door.getX()][door.getY()] = Tileset.UNLOCKED_DOOR;
        }


        for (Position c : coins) {
            world[c.getX()][c.getY()] = Tileset.COIN;
        }

        currentWorld = world;
    }

    private void drawPause() {
        StdDraw.setPenColor(new Color(80, 110, 80));
        StdDraw.filledRectangle(currentWorld.length / 2, currentWorld[0].length / 2, 12, 8);

        StdDraw.setPenColor(StdDraw.WHITE);
        Font subtitle = new Font("Subtitle", Font.BOLD, 35);
        StdDraw.setFont(subtitle);
        StdDraw.text(currentWorld.length / 2, currentWorld[0].length / 2 + 4, "Pause");

        Font body = new Font("menu", Font.PLAIN, 25);
        StdDraw.setFont(body);
        StdDraw.text(currentWorld.length / 2, currentWorld[0].length / 2,
                "Coins You Got: " + coinsGot);

        Font choice = new Font("menu", Font.PLAIN, 15);
        StdDraw.setFont(choice);
        StdDraw.text(currentWorld.length / 2, currentWorld[0].length / 2 - 3, "Play Again (P)");
        StdDraw.text(currentWorld.length / 2, currentWorld[0].length / 2 - 4, "Resume (R)");
        StdDraw.text(currentWorld.length / 2, currentWorld[0].length / 2 - 5, "Quit (Q)");

        StdDraw.show();
    }

    private void drawTypedString(String s) {
        StdDraw.clear(StdDraw.BLACK);
        Font subtitle = new Font("Subtitle", Font.PLAIN, 30);
        StdDraw.setPenColor(StdDraw.WHITE);
        StdDraw.setFont(subtitle);
        StdDraw.text(WIDTH / 2, HEIGHT / 2 + 10, "Enter any number to create a world:");
        StdDraw.text(WIDTH / 2, HEIGHT / 2, s);
        Font body = new Font("menu", Font.PLAIN, 20);
        StdDraw.setFont(body);
        StdDraw.text(WIDTH / 2, HEIGHT / 2 - 13, "Type any other key to back to the menu");

        if (s.length() > 0) {
            StdDraw.text(WIDTH / 2, HEIGHT / 2 - 10, "Type 'S' to enter this world");
        }
        if (s.equals("20201129")) {
            StdDraw.setPenColor(new Color(245, 145, 65));
            StdDraw.text(WIDTH / 2, HEIGHT / 2 - 4, egg);
        }
        StdDraw.show();
    }

    private void drawFail() {
        StdDraw.setPenColor(new Color(80, 110, 80));
        StdDraw.filledRectangle(currentWorld.length / 2, currentWorld[0].length / 2, 12, 8);

        StdDraw.setPenColor(StdDraw.WHITE);
        Font subtitle = new Font("Subtitle", Font.BOLD, 35);
        StdDraw.setFont(subtitle);
        StdDraw.text(currentWorld.length / 2, currentWorld[0].length / 2 + 2, "You Failed!");

        Font choice = new Font("menu", Font.PLAIN, 15);
        StdDraw.setFont(choice);
        StdDraw.text(currentWorld.length / 2, currentWorld[0].length / 2 - 3, "Play Again (P)");
        StdDraw.text(currentWorld.length / 2, currentWorld[0].length / 2 - 4, "Quit (Q)");

        StdDraw.show();
    }

    private void drawAbout() {
        StdDraw.clear(StdDraw.BLACK);
        StdDraw.setPenColor(StdDraw.WHITE);

        Font info = new Font("info", Font.PLAIN, 30);
        StdDraw.setFont(info);
        StdDraw.text(WIDTH / 2, 4 * HEIGHT / 5 + 2, "Dear player:");


        Font content = new Font("content", Font.PLAIN, 15);
        StdDraw.setFont(content);
        StdDraw.text(WIDTH / 2, 4 * HEIGHT / 5 - 1, "Welcome to our world!");
        StdDraw.text(WIDTH / 2, 4 * HEIGHT / 5 - 2,
                "In this world, you will explore a random generated world according to your typed code.");
        StdDraw.text(WIDTH / 2, 4 * HEIGHT / 5 - 3,
                "You can only see part of the entire world by a certain distance from your position.");
        StdDraw.text(WIDTH / 2, 4 * HEIGHT / 5 - 4,
                "You can choose easy/median/hard mode to modify how far you can see).");
        StdDraw.text(WIDTH / 2, 4 * HEIGHT / 5 - 5,
                "Your goal is to exit the door. In order to exit the door, you have to collect all the coins.");
        StdDraw.text(WIDTH / 2, 4 * HEIGHT / 5 - 6,
                "Note: You have one chance to test whether you have all the coins at the door by trying unlock it.");
        StdDraw.text(WIDTH / 2, 4 * HEIGHT / 5 - 7,
                "However, " +
                        "the second time that you tries to unlock the door without the enough coins");
        StdDraw.text(WIDTH / 2, 4 * HEIGHT / 5 - 8, "will lead a failure of this game.");

        Font name = new Font("name", Font.PLAIN, 30);
        StdDraw.setFont(name);
        StdDraw.text(WIDTH / 2 - 7, HEIGHT / 2,
                "Designers:  " + "                 " + "&");
        StdDraw.setPenColor(225,230,104);
        StdDraw.text(WIDTH / 2 - 3, HEIGHT / 2,
                "Debby Lin");
        StdDraw.setPenColor(245, 138, 66);
        StdDraw.text(WIDTH / 2 + 8, HEIGHT / 2,
                "   Mary Guo");

        StdDraw.setPenColor(StdDraw.WHITE);
        Font back = new Font("back", Font.PLAIN, 25);
        StdDraw.setFont(back);
        StdDraw.text(WIDTH / 2, HEIGHT / 2 - 10,
                "BACK TO MENU (Q)");

        StdDraw.show();
    }

    private void chooseMode(char m) {
        if (m == '1') {
            r = 5;
        } else if (m == '2') {
            r = 8;
        } else if (m == '3') {
            r = 12;
        } else if (m == '4') {
            r = -1;
        }
    }

    private void drawChooseMode() {
        StdDraw.clear(StdDraw.BLACK);
        StdDraw.setPenColor(StdDraw.WHITE);

        Font subtitle = new Font("Subtitle", Font.PLAIN, 30);
        StdDraw.setFont(subtitle);
        StdDraw.text(WIDTH / 2, HEIGHT / 2 + 10, "Enter corresponding number to choose mode:");

        Font body = new Font("menu", Font.PLAIN, 24);
        StdDraw.setFont(body);
        StdDraw.text(WIDTH / 2, HEIGHT / 2 + 3, "Hard (1)");
        StdDraw.text(WIDTH / 2, HEIGHT / 2, "Normal (2)");
        StdDraw.text(WIDTH / 2, HEIGHT / 2 - 3, "Easy (3)");
        StdDraw.text(WIDTH / 2, HEIGHT / 2 - 6, "Light Up (4)");
        StdDraw.text(WIDTH / 2, HEIGHT / 2 - 13, "Type any other key to back to the menu");

        StdDraw.show();
    }




    private LinkedList<Character> stringToCharsHelper(String input) {
        LinkedList<Character> chars = new LinkedList<>();
        for (char ch : input.toCharArray()) {
            chars.addLast(ch);
        }
        return chars;

    }

    /**
     * Method used for autograding and testing your code. The input string will be a series
     * of characters (for example, "n123sswwdasdassadwas", "n123sss:q", "lwww". The engine should
     * behave exactly as if the user typed these characters into the engine using
     * interactWithKeyboard.
     * <p>
     * Recall that strings ending in ":q" should cause the game to quite save. For example,
     * if we do interactWithInputString("n123sss:q"), we expect the game to run the first
     * 7 commands (n123sss) and then quit and save. If we then do
     * interactWithInputString("l"), we should be back in the exact same state.
     * <p>
     * In other words, both of these calls:
     * - interactWithInputString("n123sss:q")
     * - interactWithInputString("lww")
     * <p>
     * should yield the exact same world state as:
     * - interactWithInputString("n123sssww")
     *
     * @param input the input string to feed to your program
     * @return the 2D TETile[][] representing the state of the world
     */
    public TETile[][] interactWithInputString(String input) {
        LinkedList<Character> inputCh = stringToCharsHelper(input.toLowerCase());

        inGame = false;
        String ss = "";

        while (!inputCh.isEmpty()) {
            char ch = inputCh.removeFirst();
            if (ch == ':') {
                char ch1 = inputCh.removeFirst();
                if (ch1 == 'q') {
                    quitSaving();
                    break;
                }
            } else if (ch == 'q') {
                if (inGame && !paused && !levelCleared && !failed) {
                    continue;
                } else {
                    inGame = false;
                }
            } else if (!inGame) {
                if (ch == 'n') {
                    char ch1 = inputCh.removeFirst();
                    if (numbers.contains(ch1)) {
                        ss += ch1;
                        while (!inputCh.isEmpty()) {
                            char ch2 = inputCh.removeFirst();
                            if (numbers.contains(ch2)) {
                                ss += ch2;
                            } else if (ch2 == 's') {
                                SEED = Long.parseLong(ss);
                                currentWorld = createNewGame(SEED);
                                break;
                            }
                        }
                    }
                } else if (ch == 'l') {
                    if (currentWorld != null) {
                        inGame = true;
                    }
                }
            } else if (inGame) {
                if (!levelCleared && !paused && !failed) {
                    if (KEY.contains(ch)) {
                        movePlayer(ch);
                        if (attemptLeft == 0) {
                            failed = true;
                        }
                    } else if (ch == 'u') {
                        paused = true;
                    }
                } else {
                    if (ch == 'p') {
                        currentWorld = createNewGame(SEED);
                        paused = false;
                        levelCleared = false;
                    } else if (paused) {
                        if (ch == 'r') {
                            paused = false;
                        }
                    } else if (levelCleared) {
                        if (ch == 'x') {
                            SEED = nextSeed;
                            currentWorld = createNewGame(SEED);
                            roundNum += 1;
                        }
                    }
                }
            }
        }
        return currentWorld;
    }

}
