package bearmaps;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.NoSuchElementException;



public class ArrayHeapMinPQ<T> implements ExtrinsicMinPQ<T> {

    private ArrayList<MinHeapNode> minHeap;
    private HashMap<T, Integer> items;

    private class MinHeapNode {
        T item;
        double priority;

        MinHeapNode(T i, double p) {
            item = i;
            priority = p;
        }

        void setPriority(double p) {
            this.priority = p;
        }

        double getPriority() {
            return priority;
        }

    }

    public ArrayHeapMinPQ() {
        minHeap = new ArrayList<>();
        items = new HashMap<>();
    }


    @Override
    public void add(T item, double priority) {
        if (contains(item)) {
            throw new IllegalArgumentException("item exists");
        } else {
            minHeap.add(new MinHeapNode(item, priority));
            items.put(item, minHeap.size() - 1);
            swim(minHeap.size() - 1);
        }
    }

    private void swap(int k1, int k2) {
        MinHeapNode node1 = minHeap.get(k1);
        MinHeapNode node2 = minHeap.get(k2);
        minHeap.set(k1, node2);
        minHeap.set(k2, node1);
        items.put(node2.item, k1);
        items.put(node1.item, k2);
    }

    private static int parent(int k) {
        if (k == 0) {
            return 0;
        }
        return (k - 1) / 2;
    }


    private void swim(int k) {
        int p = parent(k);
        if (compLess(k, p)) {
            swap(k, p);
            swim(p);
        }
    }


    /* Returns true if the PQ contains the given item. */
    @Override
    public boolean contains(T item) {
        return items.containsKey(item);
    }
    /* Returns the minimum item. Throws NoSuchElementException if the PQ is empty. */
    @Override
    public T getSmallest() {
        if (minHeap.isEmpty()) {
            throw new NoSuchElementException("No such element");
        }
        return minHeap.get(0).item;
    }

    private static int rightChild(int k) {
        return k * 2 + 2;
    }

    private static int leftChild(int k) {
        return k * 2 + 1;
    }

    private boolean compLess(int k1, int k2) {
        return minHeap.get(k1).getPriority() < minHeap.get(k2).getPriority();
    }

    private void sink(int k) {
        int temp = leftChild(k);
        if (leftChild(k) < minHeap.size()) {
            if (rightChild(k) < minHeap.size() && compLess(rightChild(k), leftChild(k))) {
                temp += 1;
            }
            if (compLess(temp, k)) {
                swap(k, temp);
                sink(temp);
            }
        }
    }

    /* Removes and returns the minimum item. Throws NoSuchElementException if the PQ is empty. */
    @Override
    public T removeSmallest() {
        if (minHeap.isEmpty()) {
            throw new NoSuchElementException("No such element");
        }
        T temp = getSmallest();
        swap(0, minHeap.size() - 1);
        minHeap.remove(minHeap.size() - 1);
        sink(0);
        items.remove(temp);
        return temp;

    }
    /* Returns the number of items in the PQ. */
    @Override
    public int size() {
        return minHeap.size();
    }
    /* Changes the priority of the given item. Throws NoSuchElementException if the item
     * doesn't exist. */
    @Override
    public void changePriority(T item, double priority) {
        if (!contains(item)) {
            throw new NoSuchElementException("Element doesn't exist");
        }
        int k = items.get(item);
        double p = minHeap.get(k).getPriority();
        minHeap.get(k).setPriority(priority);
        if (p < priority) {
            sink(k);
        } else {
            swim(k);
        }
    }

}

