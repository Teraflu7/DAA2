package daa;

import daa.structures.MinHeap;
import org.junit.jupiter.api.Test;
import java.util.Random;
import static org.junit.jupiter.api.Assertions.*;

public class MinHeapTest {

    @Test
    public void testHeapPropertyAndSortedOutput() {
        MinHeap heap = new MinHeap();
        Random rng = new Random(42);
        int n = 200;

        for (int i = 0; i < n; i++) {
            heap.insert(rng.nextInt(1000) - 500);
            assertTrue(heap.isValidHeap(), "Heap property violated during insertion");
        }

        int prev = heap.extractMin();
        for (int i = 1; i < n; i++) {
            assertTrue(heap.isValidHeap(), "Heap property violated during extraction");
            int curr = heap.extractMin();
            assertTrue(prev <= curr, "Output not in non-decreasing order: " + prev + " > " + curr);
            prev = curr;
        }
    }

    @Test
    public void testEmptyHeapExceptions() {
        MinHeap heap = new MinHeap();
        assertThrows(IllegalStateException.class, heap::peekMin);
        assertThrows(IllegalStateException.class, heap::extractMin);
    }

    @Test
    public void testFloydBuildHeap() {
        int[] data = {9, 4, 7, 1, -2, 6, 5};
        MinHeap heap = MinHeap.buildHeap(data);
        assertTrue(heap.isValidHeap());
        assertEquals(-2, heap.extractMin());
        assertEquals(1, heap.extractMin());
    }
}