package es.datastructur.synthesizer;
import java.util.Iterator;

public class ArrayRingBuffer<T> implements BoundedQueue<T> {
    /* Index for the next dequeue or peek. */
    private int first;
    /* Index for the next enqueue. */
    private int last;
    /* Variable for the fillCount. */
    private int fillCount;
    /* Array for storing the buffer data. */
    private T[] rb;

    /**
     * Create a new ArrayRingBuffer with the given capacity.
     */
    public ArrayRingBuffer(int capacity) {
        rb = (T[]) new Object[capacity];
        first = 0;
        last = 0;
        fillCount = 0;

    }

    /**
     * Adds x to the end of the ring buffer. If there is no room, then
     * throw new RuntimeException("Ring buffer overflow").
     */
    @Override
    public void enqueue(T x) {
        if (isFull()) {
            throw new RuntimeException("Ring buffer overflow");
        }
        rb[last] = x;
        last += 1;
        if (last == capacity()) {
            last = 0;
        }
        fillCount = fillCount + 1;
    }

    /**
     * Dequeue oldest item in the ring buffer. If the buffer is empty, then
     * throw new RuntimeException("Ring buffer underflow").
     */
    @Override
    public T dequeue() {
        if (isEmpty()) {
            throw new RuntimeException("Ring buffer underflow");
        }
        T remove_value = rb[first];
        rb[first] = null;
        first += 1;
        if (first == capacity()) {
            first = 0;
        }
        fillCount -= 1;
        return remove_value;
    }

    /**
     * Return oldest item, but don't remove it. If the buffer is empty, then
     * throw new RuntimeException("Ring buffer underflow").
     */
    @Override
    public T peek() {
        if (isEmpty()) {
            throw new RuntimeException("Ring buffer underflow");
        }
        return rb[first];
    }
    @Override
    public int capacity() {
        return rb.length;
    }
    public int fillCount() {
        return fillCount;
    }

    @Override
    public Iterator<T> iterator() {
        return new ArrayRingIterator();
    }

    private class ArrayRingIterator implements Iterator<T> {
        private int wizpos;
        private int size;

        public ArrayRingIterator() {
            wizpos = first;
            size = 0;
        }

        public boolean hasNext() {
            return size < fillCount();
        }
        public T next() {
            T returnItem = rb[wizpos];
            wizpos += 1;
            size += 1;
            if (wizpos == capacity()) {
                wizpos = 0;
            }
            return returnItem;
        }
    }

    @Override
    public boolean equals(Object o) {
        ArrayRingBuffer<T> other = (ArrayRingBuffer<T>) o;
        Iterator<T> I1 = this.iterator();
        Iterator<T> I2 = other.iterator();
        if (this.fillCount() == other.fillCount()) {
            return true;
        }

        while (I1.hasNext() && I2.hasNext()) {
            if (I1.next() != I2.next()) {
                return false;
            }
        }
        return true;
    }



}
