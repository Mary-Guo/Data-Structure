
public class ArrayDeque<T> {
    private T[] items;
    private int size;
    private int nextFirst;
    private int nextLast;
    private int rFactor = 2;

    /** Creates an empty array deque. */
    public ArrayDeque() {
        items = (T[]) new Object[8];
        size = 0;
        nextFirst = 4;
        nextLast = 5;
    }




    /** Adds an item of type T to the front of the deque. */
    public void addFirst(T item) {
        if (size == items.length) {
            resize(size * rFactor);
        }
        items[nextFirst] = item;
        nextFirst = sortNextFirst(nextFirst);
        size += 1;
    }



    /** Adds an item of type T to the back of the deque. */
    public void addLast(T item) {
        if (size == items.length) {
            resize(size * rFactor);
        }
        items[nextLast] = item;
        nextLast = sortNextLast(nextLast);
        size += 1;
    }
    /** Returns true if deque is empty, false otherwise. */
    public boolean isEmpty() {
        return size == 0;
    }

    /** Returns the number of items in the deque. */
    public int size() {
        return size;
    }

    /** Prints the items in the deque from first to last, separated by a space.
     * Once all the items have been printed, print out a new line. */
    public void printDeque() {
        int i = sortNextLast(nextFirst);
        while (i < items.length) {
            System.out.print(items[i] + "");
            i += sortNextLast(i);
        }
        System.out.println();
    }

    /** Removes and returns the item at the front of the deque.
     * If no such item exists, returns null. */
    public T removeFirst() {
        if (isEmpty()) {
            return null;
        }
        int temp = sortNextLast(nextFirst);
        T returnItem = items[temp];
        items[temp] = null;
        size -= 1;
        nextFirst = temp;

        if ((items.length >= 16)  && (size < items.length * 0.25)) {
            resize(items.length / rFactor);
        }
        return returnItem;
    }

    /** Removes and returns the item at the back of the deque.
     * If no such item exists, returns null. */
    public T removeLast() {
        if (isEmpty()) {
            return null;
        }
        int temp = sortNextFirst(nextLast);
        //have to assign a temp variable to store it, otherwise calling the method for
        // multiple times will result different int sortNextFirst(nextLast).
        T returnItem = items[temp];
        items[temp] = null;
        size -= 1;
        nextLast = temp;

        if (items.length >= 16 && size < items.length * 0.25) {
            resize(items.length / rFactor);
        }
        return returnItem;
    }

    /** Gets the item at the given index, where 0 is the front, 1 is the next item, and so forth.
     * If no such item exists, returns null. Must not alter the deque! */
    public T get(int i) {
        if (isEmpty() || i > size) {
            return null;
        }
        i = (sortNextLast(nextFirst) + i) % items.length;

        return items[i];
    }







    /** return the Next nextFirst */
    private int sortNextFirst(int i) {
        return (i - 1 + items.length) % items.length;

    }

    /** return the Next nextLast */
    private int sortNextLast(int i) {
        return (i + 1) % items.length;
    }

    /** resize the array */
    private void resize(int capacity) {
        T[] a = (T[]) new Object[capacity];
        int next = sortNextLast(nextFirst);
        for (int i = 0; i < size; i += 1) {
            a[i] = items[next];
            next = sortNextLast(next);
        }
        nextFirst = capacity - 1;
        nextLast = size;
        items = a;

        //System.arraycopy(items, 0, a, 0, size);

    }
}




