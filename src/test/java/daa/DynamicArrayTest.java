package daa;

import daa.structures.DynamicArray;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class DynamicArrayTest {
    private DynamicArray array;

    @BeforeEach
    public void setUp() {
        array = new DynamicArray(2);
    }

    @Test
    public void testAddAndGet() {
        array.add(10);
        array.add(20);
        array.add(30);
        assertEquals(3, array.size());
        assertEquals(10, array.get(0));
        assertEquals(20, array.get(1));
        assertEquals(30, array.get(2));
    }

    @Test
    public void testInsertAtBoundariesAndMiddle() {
        array.add(0, 10);
        array.add(1, 30);
        array.add(1, 20);
        assertEquals(10, array.get(0));
        assertEquals(20, array.get(1));
        assertEquals(30, array.get(2));
    }

    @Test
    public void testRemove() {
        array.add(1);
        array.add(2);
        array.add(3);
        int removed = array.remove(1);
        assertEquals(2, removed);
        assertEquals(2, array.size());
        assertEquals(3, array.get(1));
    }

    @Test
    public void testContains() {
        array.add(5);
        array.add(15);
        assertTrue(array.contains(5));
        assertTrue(array.contains(15));
        assertFalse(array.contains(99));
    }

    @Test
    public void testOutOfBoundsExceptions() {
        assertThrows(IndexOutOfBoundsException.class, () -> array.get(0));
        assertThrows(IndexOutOfBoundsException.class, () -> array.remove(0));
        assertThrows(IndexOutOfBoundsException.class, () -> array.add(-1, 100));
        assertThrows(IndexOutOfBoundsException.class, () -> array.add(5, 100));
    }
}