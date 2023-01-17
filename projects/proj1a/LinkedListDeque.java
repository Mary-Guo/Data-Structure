

public class LinkedListDeque<T> {
    private StaffNode sentinel;
    private int size;

    private class StaffNode {
        private StaffNode prev;
        private StaffNode next;
        private T item;

        private StaffNode(T i, StaffNode p, StaffNode n) {
            item = i;
            prev = p;
            next = n;
            System.out.println(size);
        }
    }


    /**
     * Adds an item of type T to the front of the deque. must not involve any looping or recursion
     */
    public void addFirst(T item) {
        StaffNode temp = new StaffNode(item, sentinel, sentinel.next);
        sentinel.next.prev = temp;
        sentinel.next = temp;
        size = size + 1;

    }

    /**
     * Adds an item of type T to the back of the deque. must not involve any looping or recursion
     */
    public void addLast(T item) {
        StaffNode temp = new StaffNode(item, sentinel.prev, sentinel);
        sentinel.prev.next = temp;
        sentinel.prev = temp;
        size = size + 1;
    }

    /**
     * Returns true if deque is empty, false otherwise.
     */
    public boolean isEmpty() {
        return sentinel.next == sentinel;
    }

    /**
     * Returns the number of items in the deque. constant time
     */
    public int size() {
        return size;
    }


    /**
     * Prints the items in the deque from first to last, separated by a space.
     * Once all the items have been printed, print out a new line.
     */
    public void printDeque() {
        while (sentinel.next != sentinel) {
            System.out.print(sentinel.next.item + "");
            sentinel.next = sentinel.next.next;
        }
        System.out.println();
    }

    /**
     * Removes and returns the item at the front of the deque. If no such item exists, returns null.
     * must not involve any looping or recursion
     */
    public T removeFirst() {
        if (size == 0) {
            return null;
        }
        T removeItem = sentinel.next.item;
        sentinel.next = sentinel.next.next;
        sentinel.next.prev = sentinel;
        size -= 1;
        return removeItem;

    }

    /**
     * Removes and returns the item at the back of the deque. If no such item exists, returns null.
     * must not involve any looping or recursion
     */
    public T removeLast() {
        if (size == 0) {
            return null;
        }
        T removeItem = sentinel.prev.item;
        sentinel.prev = sentinel.prev.prev;
        sentinel.prev.next = sentinel;
        size -= 1;
        return removeItem;

    }

    /**
     * Gets the item at the given index, where 0 is the front, 1 is the next item, and so forth.
     * If no such item exists, returns null. Must not alter the deque!
     */
    public T get(int i) {
        if (i >= size) {
            return null;
        }
        StaffNode getItem = sentinel.next;
        for (i = i; i > 0; i -= 1) {
            getItem = getItem.next;
        }
        return getItem.item;
    }

    /**
     * Creates an empty LinkedListDeque.
     */
    public LinkedListDeque() {
        sentinel = new StaffNode(null, null, null);
        sentinel.next = sentinel;
        sentinel.prev = sentinel;
        size = 0;
    }

    /**
     * Recursion. Gets the item at the given index, where 0 is the front,
     * 1 is the next item, and so forth.
     * If no such item exists, returns null. Must not alter the deque!
     */
    public T getRecursive(int index) {
        return helper(sentinel.next, index);
    }

    private T helper(StaffNode getItem, int index) {
        if (index >= size) {
            return null;
        } else if (index == 0) {
            return getItem.item;
        }
        return helper(getItem.next, index - 1);
    }
}



