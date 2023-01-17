package byow.lab12;
import org.junit.Test;
import static org.junit.Assert.*;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;
import org.w3c.dom.html.HTMLDOMImplementation;

import java.util.Random;

/**
 * Draws a world consisting of hexagonal regions.
 */
public class HexWorld {
    private static final int WIDTH = 30;
    private static final int HEIGHT = 30;

    private int size;

    public HexWorld(int size) {
        this.size = size;
    }

    public TETile[][] makeHexTile() {
        int height = size * 2;
        int width = size + (size - 1) * 2;
        TETile[][] hex = new TETile[width][height];
        for (int x = 0; x < width; x += 1) {
            for (int y = 0; y < height; y += 1) {
                hex[x][y] = Tileset.NOTHING;
            }
        }
        return hex;
    }

    public static void fillHex(TETile[][] hex, TETile filling) {
        int size = 3;
        for (int i = 0; i < size; i++) {
            int last = 2 * size - 1;
            int startIndex = size - 1 - i;
            int endIndex = startIndex + size + i * 2;
            for (int j = startIndex; j < endIndex; j++) {
                hex[j][i] = filling;
                hex[j][last - i] = filling;
            }
        }
    }

    public static void main(String[] args) {
        TERenderer ter = new TERenderer();
        ter.initialize(WIDTH, HEIGHT);

        HexWorld hex3 = new HexWorld(3);
        TETile[][] hexWorld = new TETile[WIDTH][HEIGHT];

        fillHex(hexWorld, Tileset.FLOWER);

        ter.renderFrame(hexWorld);
    }
}
