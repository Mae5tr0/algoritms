import org.junit.Before;
import org.junit.Test;

import java.util.Iterator;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.core.AnyOf.anyOf;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class RandomizedQueueTest {
    private RandomizedQueue<String> q;

    @Before
    public void setUp() {
        q = new RandomizedQueue<>();
    }

    @Test
    public void singleValue() {
        assertTrue("empty", q.isEmpty());
        assertTrue("size", q.size() == 0);

        q.enqueue("a");

        assertEquals("first", "a", q.dequeue());

        assertTrue("empty", q.isEmpty());
        assertTrue("size", q.size() == 0);
    }

    @Test
    public void multipleValues() {
        q.enqueue("a");
        q.enqueue("b");
        q.enqueue("c");

        assertThat(q.dequeue(), anyOf(equalTo("a"), equalTo("b"), equalTo("c")));
        assertThat(q.dequeue(), anyOf(equalTo("a"), equalTo("b"), equalTo("c")));
        assertThat(q.dequeue(), anyOf(equalTo("a"), equalTo("b"), equalTo("c")));
    }

    @Test
    public void sample() {
        q.enqueue("a");

        assertEquals("sample", "a", q.sample());
        assertTrue("size", q.size() == 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void exceptionEnqueueForNull() {
        q.enqueue(null);
    }

    @Test(expected = java.util.NoSuchElementException.class)
    public void exceptionDequeueOnEmptyItems() {
        q.dequeue();
    }

    @Test(expected = java.util.NoSuchElementException.class)
    public void exceptionSampleOnEmptyItems() {
        q.sample();
    }

    @Test(expected = java.util.NoSuchElementException.class)
    public void iteratorNextError() {
        Iterator<String> i = q.iterator();
        i.next();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void iteratorRemoveError() {
        q.iterator().remove();
    }
}

