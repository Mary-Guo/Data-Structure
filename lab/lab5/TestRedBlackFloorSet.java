import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

/**
 * Created by hug.
 */
public class TestRedBlackFloorSet {
    @Test
    public void randomizedTest() {
        AListFloorSet L1 = new AListFloorSet();
        RedBlackFloorSet L2 = new RedBlackFloorSet();
        for (int i = 0; i < 1000000; i += 1) {
            double number = StdRandom.uniform(-5000, 5000);
            L1.add(number);
            L2.add(number);
        }

        for (int i = 0; i < 100000; i += 1) {
            double number = StdRandom.uniform(-5000, 5000);
            assertEquals(L1.floor(number), L2.floor(number), 0.000001);
        }
    }
}
