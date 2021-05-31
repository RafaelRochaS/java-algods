/**
 * Class that implements HashTables, of key type String and value type String only.
 * The class has a maximum size of 1000, but can handle collisions in hash codes.
 * Implemented from scratch, without any Java Collection.
 * 
 * @author Rafael Souza
 */
public class HashTables {
    
    private static final int SIZE = 1000;

    private Node[] table = new Node[SIZE];

    /**
     * Gets the array index based on the hash code of the key.
     * @param key Key to use as basis for the index.
     * @return Hashcode obtained to use as index in the array.
     */
    private int getIndexFromHash(String key) {
        return Math.abs(key.hashCode()) % SIZE;
    }

    /**
     * Traverses a linkedlist and returns a reference to the last node in the list, using recursion.
     * Assumes the initial node is not null.
     * @param node The node to check.
     * @return A reference to the last node in the list.
     */
    private Node getLastNode(Node node) {

        if (node.next == null) {
            return node;
        } else {
            return getLastNode(node);
        }
    }

    /**
     * Traverses a linked list to get the value of a given key, using recursion.
     * Returns null if not found.
     * @param node The node to check.
     * @param key The key to check.
     * @return The value associated with the key, or null if not found.
     */
    private String getValueFromKey(Node node, String key) {

        if (node == null) {
            return null;
        } else if (node.key.equals(key)) {
            return node.value;
        } else {
            return getValueFromKey(node.next, key);
        }
    }

    /**
     * Gets the mapping of a value associated with a key.
     * @param key The key to search for.
     * @return The value associated with that key, or null if not found.
     */
    public String get(String key) {
        return getValueFromKey(table[getIndexFromHash(key)], key);
    }

    /**
     * Add to the table a new mapping of key/value.
     * 
     * @param key The key to associate the value with.
     * @param value The value to associate the key with.
     */
    public void put(String key, String value) {
        Node newNode = new Node(key, value);
        if (table[getIndexFromHash(key)] == null) {
            table[getIndexFromHash(key)] = newNode;
        } else {
            Node nodeToSet = getLastNode(table[getIndexFromHash(key)]);
            nodeToSet.next = newNode;
        }
    }

    /**
     * Removes the item in the table associated with that key. 
     * @param key The key to remove.
     * @return The value removed, or null if not found.
     */
    public String remove(String key) {
        int index = getIndexFromHash(key);

        if (table[index].next == null) {
            String value = table[index].value;
            table[index] = null;
            return value;
        } else {
            Node previous = table[index];
            Node runner = table[index].next;
            while (true) {
                if (runner.key.equals(key)) {
                    String value = runner.value;
                    previous.next = null;
                    return value;
                } else {
                    previous = runner;
                    runner = previous.next;
                }
            }
        }
    }

    /**
     * Returns whether or not the given key is in the table.
     * 
     * @param key The key to look for.
     * @return True if the key is there, false if not.
     */
    public boolean containsKey(String key) {
        return table[getIndexFromHash(key)] != null;
    }

    /**
     * Returns the amount of key/value mappings in the table. Because the table can handle collisions, 
     * this value can be larger than the size of the table.
     * @return the actual size of the table.
     */
    public int size() {

        int counter = 0;
        for (int i = 0; i < table.length; i++) {
            if (table[i] != null) {
                Node runner = table[i];
                while (true) {
                    counter++;
                    if (runner.next == null) {
                        break;
                    } else {
                        runner = runner.next;
                    }
                }
            }
        }

        return counter;
    }

    /**
     * Class to implement the linked list. All the keys and values will be stored as nodes in the list, to handle collision of hash codes.
     */
    private class Node {

        Node(String key, String value) {
            this.key = key;
            this.value = value;
        }

        String key;
        String value;
        Node next;
    }
}