package lab1.task2;

import lab1.task2.algorithms.HeapSort;

import java.util.Arrays;

public class App {
  public static void main(String[] args) {
    int[] data = {4, 1, 3, 2};
    HeapSort.SortResult result = HeapSort.sortWithTrace(data);

    System.out.println("Sorted: " + Arrays.toString(result.getSortedArray()));
    System.out.println("Trace:");
    result.getTrace().forEach(System.out::println);
  }
}
