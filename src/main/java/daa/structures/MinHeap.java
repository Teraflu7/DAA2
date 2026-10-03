package daa.structures;

import daa.metrics.Metrics;

public class MinHeap {
    private int[] heap;
    private int size;
    private final Metrics metrics;

    public MinHeap() {
        this(10);
    }

    public MinHeap(int capacity) {
        this.heap = new int[Math.max(capacity, 2)];
        this.size = 0;
        this.metrics = new Metrics();
    }

    public Metrics getMetrics() {
        return metrics;
    }

    public int size() {
        return size;
    }

    private void ensureCapacity() {
        if (size == heap.length) {
            int[] newHeap = new int[heap.length * 2];
            for (int i = 0; i < size; i++) {
                metrics.steps++;
                metrics.moves++;
                newHeap[i] = heap[i];
            }
            heap = newHeap;
        }
    }

    private void swap(int i, int j) {
        metrics.steps += 2;
        metrics.moves += 3;
        int tmp = heap[i];
        heap[i] = heap[j];
        heap[j] = tmp;
    }

    public void insert(int val) {
        ensureCapacity();
        metrics.moves++;
        heap[size] = val;
        bubbleUp(size);
        size++;
    }

    private void bubbleUp(int index) {
        while (index > 0) {
            int parent = (index - 1) / 2;
            metrics.steps += 2;
            metrics.comparisons++;
            if (heap[index] < heap[parent]) {
                swap(index, parent);
                index = parent;
            } else {
                break;
            }
        }
    }

    public int peekMin() {
        if (size == 0) {
            throw new IllegalStateException("Heap is empty");
        }
        metrics.steps++;
        return heap[0];
    }

    public int extractMin() {
        if (size == 0) {
            throw new IllegalStateException("Heap is empty");
        }
        metrics.steps++;
        int min = heap[0];
        metrics.steps++;
        metrics.moves++;
        heap[0] = heap[size - 1];
        size--;
        if (size > 0) {
            bubbleDown(0);
        }
        return min;
    }

    private void bubbleDown(int index) {
        while (index * 2 + 1 < size) {
            int left = index * 2 + 1;
            int right = index * 2 + 2;
            int smallest = index;

            metrics.steps += 2;
            metrics.comparisons++;
            if (heap[left] < heap[smallest]) {
                smallest = left;
            }

            if (right < size) {
                metrics.steps += 2;
                metrics.comparisons++;
                if (heap[right] < heap[smallest]) {
                    smallest = right;
                }
            }

            if (smallest != index) {
                swap(index, smallest);
                index = smallest;
            } else {
                break;
            }
        }
    }

    // Task B: O(n) Floyd's bottom-up algorithm (+5% bonus)
    public static MinHeap buildHeap(int[] array) {
        MinHeap h = new MinHeap(array.length);
        h.size = array.length;
        for (int i = 0; i < array.length; i++) {
            h.metrics.steps++;
            h.metrics.moves++;
            h.heap[i] = array[i];
        }
        for (int i = (h.size / 2) - 1; i >= 0; i--) {
            h.bubbleDown(i);
        }
        return h;
    }

    // Проверка выполнения основного инварианта кучи
    public boolean isValidHeap() {
        for (int i = 0; i <= (size - 2) / 2; i++) {
            int left = 2 * i + 1;
            int right = 2 * i + 2;
            if (left < size && heap[i] > heap[left]) return false;
            if (right < size && heap[i] > heap[right]) return false;
        }
        return true;
    }
}