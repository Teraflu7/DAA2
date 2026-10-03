package daa.structures;

import daa.metrics.IntList;
import daa.metrics.Metrics;

public class DynamicArray implements IntList {
    private int[] data;
    private int size;
    private final Metrics metrics;

    public DynamicArray() {
        this(10);
    }

    public DynamicArray(int initialCapacity) {
        this.data = new int[Math.max(initialCapacity, 2)];
        this.size = 0;
        this.metrics = new Metrics();
    }

    @Override
    public Metrics getMetrics() {
        return metrics;
    }

    @Override
    public int size() {
        return size;
    }

    private void ensureCapacity() {
        if (size == data.length) {
            int[] newData = new int[data.length * 2];
            for (int i = 0; i < size; i++) {
                metrics.steps++;
                metrics.moves++;
                newData[i] = data[i];
            }
            data = newData;
        }
    }

    @Override
    public void add(int element) {
        ensureCapacity();
        metrics.moves++;
        data[size++] = element;
    }

    @Override
    public void add(int index, int element) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
        ensureCapacity();
        for (int i = size; i > index; i--) {
            metrics.steps++;
            metrics.moves++;
            data[i] = data[i - 1];
        }
        metrics.moves++;
        data[index] = element;
        size++;
    }

    @Override
    public int remove(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
        metrics.steps++;
        int removed = data[index];
        for (int i = index; i < size - 1; i++) {
            metrics.steps++;
            metrics.moves++;
            data[i] = data[i + 1];
        }
        size--;
        return removed;
    }

    @Override
    public int get(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
        metrics.steps++;
        return data[index];
    }

    @Override
    public boolean contains(int element) {
        for (int i = 0; i < size; i++) {
            metrics.steps++;
            metrics.comparisons++;
            if (data[i] == element) {
                return true;
            }
        }
        return false;
    }
}