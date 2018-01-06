import java.util.Iterator;

public class Deque<Item> implements Iterable<Item> {

    private Node first, last;
    private int length;

    private class Node {
        Item item;
        Node next;
        Node prev;

        public Node(Item nodeItem) {
            item = nodeItem;
        }
    }

    // construct an empty deque
    public Deque() {
        length = 0;
    }

    // is the deque empty?
    public boolean isEmpty() {
        return length == 0;
    }

    // return the number of items on the deque
    public int size() {
        return length;
    }

    // add the item to the front
    public void addFirst(Item item) {
        if (item == null) throw new java.lang.IllegalArgumentException();

        Node newNode = new Node(item);
        if (isEmpty()) {
            last = newNode;
            first = newNode;
        } else {
            Node oldFirst = first;
            first = newNode;
            newNode.next = oldFirst;
            oldFirst.prev = newNode;
        }
        length++;
    }

    // add the item to the end
    public void addLast(Item item) {
        if (item == null) throw new java.lang.IllegalArgumentException();

        Node newNode = new Node(item);
        if (isEmpty()) {
            last = newNode;
            first = newNode;
        } else {
            Node oldLast = last;
            last = newNode;
            newNode.prev = oldLast;
            oldLast.next = newNode;
        }
        length++;
    }

    // remove and return the item from the front
    public Item removeFirst() {
        if (isEmpty()) throw new java.util.NoSuchElementException();

        Node firstNode = first;
        Node newFirstNode = firstNode.next;
        if (newFirstNode == null) {
            first = null;
            last = null;
        } else {
            newFirstNode.prev = null;
            first = newFirstNode;
        }
        length--;

        return firstNode.item;
    }

    // remove and return the item from the end
    public Item removeLast() {
        if (isEmpty()) throw new java.util.NoSuchElementException();

        Node lastNode = last;
        Node newLastNode = lastNode.prev;
        if (newLastNode == null) {
            first = null;
            last = null;
        } else {
            newLastNode.next = null;
            last = newLastNode;
        }
        length--;

        return lastNode.item;
    }

    // return an iterator over items in order from front to end
    public Iterator<Item> iterator() {
        return new DequeIterator();
    }

    private class DequeIterator implements Iterator<Item> {
        private Node current = first;

        public boolean hasNext() {
            return current != null;
        }

        public Item next() {
            if (!hasNext()) throw new java.util.NoSuchElementException();

            Item item = current.item;
            current = current.next;
            return item;
        }
    }

    // unit testing (optional)
    public static void main(String[] args) {

    }
}