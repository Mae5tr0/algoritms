import java.util.Iterator;

import edu.princeton.cs.algs4.StdRandom;

public class RandomizedQueue<Item> implements Iterable<Item> {
    private Item[] a;         // array of items
    private int n;            // number of elements on stack

    // construct an empty randomized queue
    public RandomizedQueue() {
        a = (Item[]) new Object[2];
        n = 0;
    }

    // is the randomized queue empty?
    public boolean isEmpty() {
        return n == 0;
    }

    // return the number of items on the randomized queue
    public int size() {
        return n;
    }

    // add the item
    public void enqueue(Item item) {
        if (item == null) throw new java.lang.IllegalArgumentException();

        if (n == a.length) resize(2 * a.length);    // double size of array if necessary
        a[n++] = item;
    }

    // remove and return a random item
    public Item dequeue() {
        if (size() == 0) throw new java.util.NoSuchElementException();

        int index = StdRandom.uniform(0, n);
        Item result = a[index];
        a[index] = a[n - 1];
        a[n - 1] = null;
        n--;
        if (n > 0 && n == a.length / 4) resize(a.length / 2);
        return result;
    }

    // return a random item (but do not remove it)
    public Item sample() {
        if (size() == 0) throw new java.util.NoSuchElementException();

        return a[StdRandom.uniform(0, n)];
    }

    // return an independent iterator over items in random order
    public Iterator<Item> iterator() {
        return new RandomizedQueueIterator();
    }

    private class RandomizedQueueIterator implements Iterator<Item> {
        private Item[] copy;
        private int i;

        public RandomizedQueueIterator() {
            copy = java.util.Arrays.copyOf(a, a.length);
            i = n;
            StdRandom.shuffle(copy);
        }

        public boolean hasNext() {
            return i > 0;
        }

        public Item next() {
            if (!hasNext()) throw new java.util.NoSuchElementException();

            return copy[i--];
        }
    }

    private void resize(int capacity) {
        // textbook implementation
        Item[] temp = (Item[]) new Object[capacity];
        for (int i = 0; i < n; i++) {
            temp[i] = a[i];
        }
        a = temp;
    }

    // unit testing (optional)
    public static void main(String[] args) {

    }
}
