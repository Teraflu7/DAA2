package daa.benchmark;

import daa.structures.DynamicArray;
import daa.structures.MinHeap;
import daa.structures.MyLinkedList;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Random;

public class BenchmarkRunner {
    private static final int[] SIZES = {100, 1000, 10000, 100000};
    private static final int RUNS = 5;

    public static void main(String[] args) throws Exception {
        File dir = new File("results");
        if (!dir.exists()) {
            dir.mkdirs();
        }

        try (PrintWriter writer = new PrintWriter(new FileWriter("results/results.csv"))) {
            writer.println("workload,variant,structure,n,time_ms,steps,moves,comparisons");

            for (int n : SIZES) {
                System.out.println("Processing n = " + n + "...");
                runWorkload1(writer, n);
                runWorkload2(writer, n);
                runWorkload3(writer, n);
                runWorkload4(writer, n);
            }
        }
        System.out.println("Benchmark completed! Results saved to results/results.csv");
    }

    private static int[] generateData(int n) {
        Random rng = new Random(42);
        int[] arr = new int[n];
        for (int i = 0; i < n; i++) {
            arr[i] = rng.nextInt();
        }
        return arr;
    }

    private static double median(double[] m) {
        Arrays.sort(m);
        return m[m.length / 2];
    }

    // W1: Random Access - 10 000 get(index)
    private static void runWorkload1(PrintWriter writer, int n) {
        int[] data = generateData(n);
        int queries = 10000;
        Random rng = new Random(42);
        int[] indices = new int[queries];
        for (int i = 0; i < queries; i++) {
            indices[i] = rng.nextInt(n);
        }

        // DynamicArray
        for (int r = 0; r < 2; r++) { // Warm-up
            DynamicArray da = new DynamicArray(n);
            for (int x : data) da.add(x);
            for (int idx : indices) da.get(idx);
        }
        double[] timesDA = new double[RUNS];
        long stepsDA = 0, movesDA = 0, compDA = 0;
        for (int r = 0; r < RUNS; r++) {
            DynamicArray da = new DynamicArray(n);
            for (int x : data) da.add(x);
            da.getMetrics().reset();
            long t0 = System.nanoTime();
            for (int idx : indices) da.get(idx);
            timesDA[r] = (System.nanoTime() - t0) / 1e6;
            stepsDA = da.getMetrics().steps;
            movesDA = da.getMetrics().moves;
            compDA = da.getMetrics().comparisons;
        }
        writer.printf("W1,default,DynamicArray,%d,%.4f,%d,%d,%d\n", n, median(timesDA), stepsDA, movesDA, compDA);

        // MyLinkedList
        for (int r = 0; r < 2; r++) { // Warm-up
            MyLinkedList list = new MyLinkedList();
            for (int x : data) list.add(x);
            for (int i = 0; i < Math.min(100, queries); i++) list.get(indices[i]);
        }
        double[] timesLL = new double[RUNS];
        long stepsLL = 0, movesLL = 0, compLL = 0;
        for (int r = 0; r < RUNS; r++) {
            MyLinkedList list = new MyLinkedList();
            for (int x : data) list.add(x);
            list.getMetrics().reset();
            long t0 = System.nanoTime();
            for (int idx : indices) list.get(idx);
            timesLL[r] = (System.nanoTime() - t0) / 1e6;
            stepsLL = list.getMetrics().steps;
            movesLL = list.getMetrics().moves;
            compLL = list.getMetrics().comparisons;
        }
        writer.printf("W1,default,MyLinkedList,%d,%.4f,%d,%d,%d\n", n, median(timesLL), stepsLL, movesLL, compLL);
    }

    // W2: Search - 1 000 contains(x)
    private static void runWorkload2(PrintWriter writer, int n) {
        int[] data = generateData(n);
        int queries = 1000;
        Random rng = new Random(42);
        int[] queryVals = new int[queries];
        for (int i = 0; i < queries / 2; i++) {
            queryVals[i] = data[rng.nextInt(n)];
        }
        for (int i = queries / 2; i < queries; i++) {
            queryVals[i] = rng.nextInt() + 100000000;
        }

        // DynamicArray
        double[] timesDA = new double[RUNS];
        long stepsDA = 0, movesDA = 0, compDA = 0;
        for (int r = 0; r < RUNS; r++) {
            DynamicArray da = new DynamicArray(n);
            for (int x : data) da.add(x);
            da.getMetrics().reset();
            long t0 = System.nanoTime();
            for (int v : queryVals) da.contains(v);
            timesDA[r] = (System.nanoTime() - t0) / 1e6;
            stepsDA = da.getMetrics().steps;
            movesDA = da.getMetrics().moves;
            compDA = da.getMetrics().comparisons;
        }
        writer.printf("W2,default,DynamicArray,%d,%.4f,%d,%d,%d\n", n, median(timesDA), stepsDA, movesDA, compDA);

        // MyLinkedList
        double[] timesLL = new double[RUNS];
        long stepsLL = 0, movesLL = 0, compLL = 0;
        for (int r = 0; r < RUNS; r++) {
            MyLinkedList list = new MyLinkedList();
            for (int x : data) list.add(x);
            list.getMetrics().reset();
            long t0 = System.nanoTime();
            for (int v : queryVals) list.contains(v);
            timesLL[r] = (System.nanoTime() - t0) / 1e6;
            stepsLL = list.getMetrics().steps;
            movesLL = list.getMetrics().moves;
            compLL = list.getMetrics().comparisons;
        }
        writer.printf("W2,default,MyLinkedList,%d,%.4f,%d,%d,%d\n", n, median(timesLL), stepsLL, movesLL, compLL);
    }

    // W3: Insert & Remove - head and middle
    private static void runWorkload3(PrintWriter writer, int n) {
        int[] data = generateData(n);
        String[] variants = {"head", "middle"};

        for (String variant : variants) {
            // DynamicArray
            double[] timesDA = new double[RUNS];
            long stepsDA = 0, movesDA = 0, compDA = 0;
            for (int r = 0; r < RUNS; r++) {
                DynamicArray da = new DynamicArray(n + 1000);
                for (int x : data) da.add(x);
                da.getMetrics().reset();
                long t0 = System.nanoTime();
                for (int i = 0; i < 1000; i++) {
                    int idx = variant.equals("head") ? 0 : da.size() / 2;
                    da.add(idx, i);
                }
                for (int i = 0; i < 1000; i++) {
                    int idx = variant.equals("head") ? 0 : da.size() / 2;
                    da.remove(idx);
                }
                timesDA[r] = (System.nanoTime() - t0) / 1e6;
                stepsDA = da.getMetrics().steps;
                movesDA = da.getMetrics().moves;
                compDA = da.getMetrics().comparisons;
            }
            writer.printf("W3,%s,DynamicArray,%d,%.4f,%d,%d,%d\n", variant, n, median(timesDA), stepsDA, movesDA, compDA);

            // MyLinkedList
            double[] timesLL = new double[RUNS];
            long stepsLL = 0, movesLL = 0, compLL = 0;
            for (int r = 0; r < RUNS; r++) {
                MyLinkedList list = new MyLinkedList();
                for (int x : data) list.add(x);
                list.getMetrics().reset();
                long t0 = System.nanoTime();
                for (int i = 0; i < 1000; i++) {
                    int idx = variant.equals("head") ? 0 : list.size() / 2;
                    list.add(idx, i);
                }
                for (int i = 0; i < 1000; i++) {
                    int idx = variant.equals("head") ? 0 : list.size() / 2;
                    list.remove(idx);
                }
                timesLL[r] = (System.nanoTime() - t0) / 1e6;
                stepsLL = list.getMetrics().steps;
                movesLL = list.getMetrics().moves;
                compLL = list.getMetrics().comparisons;
            }
            writer.printf("W3,%s,MyLinkedList,%d,%.4f,%d,%d,%d\n", variant, n, median(timesLL), stepsLL, movesLL, compLL);
        }
    }

    // W4: Priority Processing - MinHeap
    private static void runWorkload4(PrintWriter writer, int n) {
        int[] data = generateData(n);
        double[] times = new double[RUNS];
        long steps = 0, moves = 0, comp = 0;

        for (int r = 0; r < RUNS; r++) {
            MinHeap heap = new MinHeap(n);
            heap.getMetrics().reset();
            long t0 = System.nanoTime();
            for (int x : data) heap.insert(x);
            int prev = heap.extractMin();
            for (int i = 1; i < n; i++) {
                int curr = heap.extractMin();
                if (prev > curr) throw new IllegalStateException("MinHeap order violated!");
                prev = curr;
            }
            times[r] = (System.nanoTime() - t0) / 1e6;
            steps = heap.getMetrics().steps;
            moves = heap.getMetrics().moves;
            comp = heap.getMetrics().comparisons;
        }
        writer.printf("W4,default,MinHeap,%d,%.4f,%d,%d,%d\n", n, median(times), steps, moves, comp);
    }
}