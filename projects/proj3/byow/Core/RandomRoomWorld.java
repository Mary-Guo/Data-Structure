package byow.Core;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;

public class RandomRoomWorld {
    private static TETile[][] world;

    public static void main(String[] args) {
        Engine engine = new Engine();
        engine.interactWithKeyboard();
/*
        world = engine.interactWithInputString("N999SDDD:Q");
        world = engine.interactWithInputString("L:Q");
        world = engine.interactWithInputString("LQL:Q");
        world = engine.interactWithInputString("LWWWDDD");
        world = engine.interactWithInputString("N999SDDDWWWDDD");
        TERenderer ter = new TERenderer();
        ter.initialize(world.length, world[0].length);
        ter.renderFrame(world);
        //*/
    }
}
