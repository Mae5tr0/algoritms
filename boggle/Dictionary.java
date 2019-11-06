public class Dictionary {
    private static final int R = 26;

    private Node root;      // root of trie
    private int n;          // number of keys in trie

    // R-way trie node
    private static class Node {
        private Boolean val = false;
        private Node[] next = new Node[R];
    }

    /**
     * Initializes an dictionary by array of words
     */
    public Dictionary(String[] words) {
        for (String word : words) {
            put(word);
        }
    }

    /**
     * Inserts the key-value pair into the symbol table, overwriting the old value
     * with the new value if the key is already in the symbol table.
     * @param key the key
     * @throws IllegalArgumentException if {@code key} is {@code null}
     */
    public void put(String key) {
        if (key == null) throw new IllegalArgumentException("first argument to put() is null");
        else root = put(root, key, 0);
    }

    private Node put(Node x, String key, int d) {
        if (x == null) x = new Node();
        if (d == key.length()) {
            if (x.val == null) n++;
            x.val = true;
            return x;
        }
        int c = key.charAt(d) - 65;
        x.next[c] = put(x.next[c], key, d+1);
        return x;
    }

    /**
     * Does this symbol table contain the given key?
     * @param key the key
     * @return {@code 1} if this symbol table contains {@code key}
     *     {@code 0} key match as prefix, but it still not full word
     *     {@code -1} key does not match anything
     * @throws IllegalArgumentException if {@code key} is {@code null}
     */
    public int contains(String key) {
        Node x = get(root, key, 0);

        if (x == null) return -1;
        return x.val ? 1 : 0;
    }

    private Node get(Node x, String key, int d) {
        if (x == null) return null;
        if (d == key.length()) return x;
        int c = key.charAt(d) - 65;
        return get(x.next[c], key, d+1);
    }
}
