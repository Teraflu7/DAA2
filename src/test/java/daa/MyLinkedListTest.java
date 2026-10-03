package daa;

import daa.structures.MyLinkedList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class MyLinkedListTest {
    private MyLinkedList list;

    @BeforeEach
    public void setUp() {
        list = new MyLinkedList();
    }

    @Test
    public void testAddAndGet() {
        list.add(100);
        list.add(200);
        list.add(300);
        assertEquals(3, list.size());
        assertEquals(100, list.get(0));
        assertEquals(200, list.get(1));
        assertEquals(300, list.get(2));
    }

    @Test
    public void testAddAtIndex() {
        list.add(0, 10);
        list.add(1, 30);
        list.add(1, 20);
        assertEquals(10, list.get(0));
        assertEquals(20, list.get(1));
        assertEquals(30, list.get(2));
    }

    @Test
    public void testRemoveHeadAndTail() {
        list.add(1);
        list.add(2);
        list.add(3);
        assertEquals(1, list.remove(0));
        assertEquals(3, list.remove(1));
        assertEquals(1, list.size());
        assertEquals(2, list.get(0));
    }

    @Test
    public void testContains() {
        list.add(42);
        list.add(84);
        assertTrue(list.contains(42));
        assertTrue(list.contains(84));
        assertFalse(list.contains(999));
    }

    @Test
    public void testExceptions() {
        assertThrows(IndexOutOfBoundsException.class, () -> list.get(0));
        assertThrows(IndexOutOfBoundsException.class, () -> list.remove(0));
        assertThrows(IndexOutOfBoundsException.class, () -> list.add(1, 10));
    }
}