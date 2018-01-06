import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.Before;

import java.util.Iterator;

public class DequeTest {
    private Deque<String> q;

    @Before
    public void setUp() {
        q = new Deque<>();
    }

    @Test
    public void fifo() {
        assertTrue("empty", q.isEmpty());
        assertTrue("size", q.size() == 0);

        q.addFirst("a");
        q.addFirst("b");
        q.addFirst("c");

        assertEquals("first", "c", q.removeFirst());
        assertEquals("second", "b", q.removeFirst());
        assertEquals("third", "a", q.removeFirst());

        assertTrue("empty", q.isEmpty());
        assertTrue("size", q.size() == 0);
    }

    @Test
    public void filo() {
        assertTrue("empty", q.isEmpty());
        assertTrue("size", q.size() == 0);

        q.addFirst("a");
        q.addFirst("b");
        q.addFirst("c");

        assertEquals("first", "a", q.removeLast());
        assertEquals("second", "b", q.removeLast());
        assertEquals("third", "c", q.removeLast());

        assertTrue("empty", q.isEmpty());
        assertTrue("size", q.size() == 0);
    }

    @Test
    public void noiseOnBound() {
        q.addFirst("a");
        assertEquals("start - end", "a", q.removeLast());

        q.addLast("a");
        assertEquals("end - start", "a", q.removeFirst());

        q.addLast("a");
        q.addLast("b");

        assertEquals("end - end", "b", q.removeLast());
        assertEquals("end - start", "a", q.removeFirst());
    }

    @Test(expected = java.lang.IllegalArgumentException.class)
    public void exceptionAddFirstForNull() {
        q.addFirst(null);
    }

    @Test(expected = java.lang.IllegalArgumentException.class)
    public void exceptionAddLastForNull() {
        q.addLast(null);
    }

    @Test(expected = java.util.NoSuchElementException.class)
    public void exceptionRemoveFirstOnEmptyItems() {
        q.removeFirst();
    }

    @Test(expected = java.util.NoSuchElementException.class)
    public void exceptionRemoveLastOnEmptyItems() {
        q.removeLast();
    }

    @Test(expected = java.util.NoSuchElementException.class)
    public void iteratorNextError() {
        Iterator<String> i = q.iterator();
        i.next();
    }

    @Test(expected = java.lang.UnsupportedOperationException.class)
    public void iteratorRemoveError() {
        q.iterator().remove();
    }
}

