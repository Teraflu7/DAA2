package daa.metrics;

public interface IntList {
    void add(int element);
    void add(int index, int element);
    int remove(int index);
    int get(int index);
    boolean contains(int element);
    int size();
    Metrics getMetrics();
}