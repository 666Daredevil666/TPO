package lab1.task2.benchmarks;

import lab1.task2.algorithms.HeapSort;
import org.openjdk.jmh.annotations.*;

import java.util.Random;
import java.util.concurrent.TimeUnit;

@State(Scope.Benchmark)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
public class HeapSortBenchmark {

    @Param({"1000", "10000", "100000", "1000000"})
    private int size;

    private int[] source;

    @Setup(Level.Trial)
    public void setUp() {
        Random random = new Random(42);
        source = new int[size];
        for (int i = 0; i < size; i++) {
            source[i] = random.nextInt();
        }
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    public int[] averageTimeHeapSort() {
        int[] copy = source.clone();
        return HeapSort.sort(copy);
    }

    @Benchmark
    @BenchmarkMode(Mode.Throughput)
    public int[] throughputHeapSort() {
        int[] copy = source.clone();
        return HeapSort.sort(copy);
    }
}