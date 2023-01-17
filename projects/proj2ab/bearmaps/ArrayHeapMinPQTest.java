package bearmaps;

import org.junit.Test;

import edu.princeton.cs.algs4.Stopwatch;

import static org.junit.Assert.*;

public class ArrayHeapMinPQTest {

    @Test
    public void addSizeTest() {
        ArrayHeapMinPQ<Integer> minPQ = new ArrayHeapMinPQ<>();
        minPQ.add(1, 1);
        minPQ.add(3, 2);
        minPQ.add(5, 3);
        assertEquals(3, minPQ.size());
        minPQ.add(7, 4);
        assertEquals(4, minPQ.size());
        minPQ.removeSmallest();
        minPQ.removeSmallest();
        minPQ.removeSmallest();
        assertEquals(1, minPQ.size());
    }

    @Test
    public void testContains() {
        ArrayHeapMinPQ<Integer> minPQ = new ArrayHeapMinPQ<>();
        minPQ.add(1, 1);
        minPQ.add(2, 2);
        minPQ.add(3, 3);
        minPQ.add(4, 4);
        assertTrue(minPQ.contains(2));
        assertFalse(minPQ.contains(0));
    }

    @Test
    public void testGetSmallest() {
        ArrayHeapMinPQ<Integer> minHeap = new ArrayHeapMinPQ<>();
        minHeap.add(1, 1);
        minHeap.add(2, 2);
        minHeap.add(3, 3);
        minHeap.add(4, 4);
        minHeap.add(5, 0);
        minHeap.add(6, 3.5);
        assertEquals(5, (int) minHeap.getSmallest());
    }

    @Test
    public void testRemoveSmallest() {
        ArrayHeapMinPQ<Integer> minPQ = new ArrayHeapMinPQ<>();
        minPQ.add(1, 1);
        minPQ.add(2, 8);
        minPQ.add(3, 3);
        minPQ.add(4, 6);
        minPQ.add(5, 7);
        minPQ.add(6, 5);
        minPQ.add(7, 4);
        minPQ.add(8, 2);
        assertEquals(1, (int) minPQ.removeSmallest());
        assertEquals(8, (int) minPQ.removeSmallest());
        assertEquals(3, (int) minPQ.removeSmallest());
        assertEquals(7, (int) minPQ.removeSmallest());
        assertEquals(6, (int) minPQ.removeSmallest());
        assertEquals(4, (int) minPQ.removeSmallest());
        assertEquals(5, (int) minPQ.removeSmallest());
        assertEquals(2, (int) minPQ.removeSmallest());
    }

    @Test
    public void testChangePriority() {
        ArrayHeapMinPQ<Integer> minPQ = new ArrayHeapMinPQ<>();
        minPQ.add(1, 1);
        minPQ.add(2, 2);
        minPQ.add(3, 3);
        minPQ.add(4, 4);
        minPQ.add(5, 0);
        minPQ.changePriority(4, 0);
        assertEquals(5, (int) minPQ.getSmallest());
        assertEquals(5, (int) minPQ.removeSmallest());
        assertEquals(4, (int) minPQ.getSmallest());
        assertEquals(4, (int) minPQ.removeSmallest());
    }

    @Test
    public void testRunTime() {
        Stopwatch sw = new Stopwatch();
        ArrayHeapMinPQ<Integer> minPQ = new ArrayHeapMinPQ<>();
        minPQ.add(1, 1);
        minPQ.add(2, 2);
        minPQ.add(3, 3);
        minPQ.add(4, 4);
        minPQ.add(5, 0);
        minPQ.add(6, 3);
        minPQ.changePriority(6, 0);
        assertEquals(5, (int) minPQ.getSmallest());
        assertEquals(5, (int) minPQ.removeSmallest());
        assertEquals(6, (int) minPQ.getSmallest());
        assertEquals(6, (int) minPQ.removeSmallest());
        System.out.println("Total time elapsed: " + sw.elapsedTime() +  " seconds.");
    }





}
