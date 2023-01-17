import java.util.Iterator;
import java.util.Set;

public class BSTMap <K extends Comparable <K>, V> implements Map61B <K, V> {
    private int size;
    private Node root;
    private class Node {
        K key;
        V value;
        Node left, right;
        Node(K k, V v) {
            key = k;
            value = v;
        }
    }
    @Override
    public void clear() {
        root = null;
        size = 0;
    }

    /* Returns true if this map contains a mapping for the specified key. */
    @Override
    public boolean containsKey(K key) { return get(key) != null; }

    /* Returns the value to which the specified key is mapped, or null if this
     * map contains no mapping for the key.
     */
    @Override
    public V get(K key) {
        return getHelper(key, root);
    }

    private V getHelper(K key, Node p) {
        if (p == null) {
            return null;
        }
        int cmp = key.compareTo(p.key);
        if (cmp > 0) {
            return getHelper(key, p.right);
        } else if (cmp < 0) {
            return getHelper(key, p.left);
        } else {
            return p.value;
        }

    }

    /* Returns the number of key-value mappings in this map. */
    @Override
    public int size() { return size; }

    /* Associates the specified value with the specified key in this map. */
    public void put(K key, V value) {
        root = putHelper(key, value, root);
    }

    private Node putHelper(K key, V value, Node p) {
        if (p == null) {
            size ++;
            return new Node(key, value);
        }
        int cmp = key.compareTo(p.key);
        if (cmp > 0) {
            p.right = putHelper(key, value, p.right);
        } else if (cmp < 0) {
            p.left = putHelper(key, value, p.left);
        } else {
            p.value = value;
        }
        return p;
    }

    /* Returns a Set view of the keys contained in this map. Not required for Lab 7.
     * If you don't implement this, throw an UnsupportedOperationException. */
    public Set<K> keySet() {
        throw new UnsupportedOperationException();
    }

    /* Removes the mapping for the specified key from this map if present.
     * Not required for Lab 7. If you don't implement this, throw an
     * UnsupportedOperationException. */
    public V remove(K key) {
        throw new UnsupportedOperationException();
    }

    /* Removes the entry for the specified key only if it is currently mapped to
     * the specified value. Not required for Lab 7. If you don't implement this,
     * throw an UnsupportedOperationException.*/
    public V remove(K key, V value) {
        throw new UnsupportedOperationException();
    }
    public Iterator<K> iterator() { throw new UnsupportedOperationException(); }
}
