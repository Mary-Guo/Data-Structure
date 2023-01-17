import static org.junit.Assert.*;
import org.junit.Test;

public class TestArrayDequeGold {
    StudentArrayDeque<Integer> sad1 = new StudentArrayDeque<>();
    ArrayDequeSolution<Integer> sad2 = new ArrayDequeSolution<>();


    @Test
    public void testArrayDequeGold() { //@source from Launcher file
        String msg = "";
        for (int i = 0; i < 10; i += 1) {
            double numberBetweenZeroAndOne = StdRandom.uniform();

            if (numberBetweenZeroAndOne < 0.5) {
                msg += " \n addLast(" + i + ") ";
                sad1.addLast(i);
                sad2.addLast(i);

            } else {
                msg += " \n addFirst(" + i + ") ";
                sad1.addFirst(i);
                sad2.addFirst(i);
            }
        }
        Integer a = sad1.removeFirst(); Integer b = sad2.removeFirst(); msg += " \n removeFirst() ";
        assertEquals(msg + " expected " + b + "," + " actual " + a, a, b);
        Integer c = sad1.removeFirst(); Integer d = sad2.removeFirst(); msg += " \n removeFirst() ";
        assertEquals(msg + " expected " + d + "," + " actual " + c, c, d);
        Integer e = sad1.removeFirst(); Integer f = sad2.removeFirst(); msg += " \n removeFirst() ";
        assertEquals(msg + " expected " + f + "," + " actual " + e, e, f);
        Integer g = sad1.removeFirst(); Integer h = sad2.removeFirst(); msg += " \n removeFirst() ";
        assertEquals(msg + " expected " + h + "," + " actual " + g, g, h);
        Integer i = sad1.removeLast(); Integer j = sad2.removeLast(); msg += " \n removeLast() ";
        assertEquals(msg + " \n expected " + j + "," + " actual " + i, i, j);


    }

}
