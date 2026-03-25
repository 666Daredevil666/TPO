package lab1.task2.algorithms;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public final class HeapSort {

  private HeapSort() {
  }

  public static int[] sort(int[] source) {
    int[] array = Arrays.copyOf(source, source.length);
    buildMaxHeap(array);

    for (int i = array.length - 1; i > 0; i--) {
      swap(array, 0, i);
      heapify(array, i, 0);
    }

    return array;
  }

  private static void buildMaxHeap(int[] array) {
    for (int i = array.length / 2 - 1; i >= 0; i--) {
      heapify(array, array.length, i);
    }
  }

  private static void heapify(int[] array, int heapSize, int index) {
    int largest = index;
    int left = 2 * index + 1;
    int right = 2 * index + 2;

    if (left < heapSize && array[left] > array[largest]) {
      largest = left;
    }

    if (right < heapSize && array[right] > array[largest]) {
      largest = right;
    }

    if (largest != index) {
      swap(array, index, largest);
      heapify(array, heapSize, largest);
    }
  }

  public static SortResult sortWithTrace(int[] source) {
    int[] array = Arrays.copyOf(source, source.length);
    List<String> trace = new ArrayList<>();
    trace.add("START");

    buildMaxHeap(array, trace);

    for (int i = array.length - 1; i > 0; i--) {
      trace.add("EXTRACT(" + i + ")");
      trace.add("SWAP(0," + i + ")");
      swap(array, 0, i);
      heapify(array, i, 0, trace);
    }

    trace.add("END");
    return new SortResult(array, trace);
  }

  private static void buildMaxHeap(int[] array, List<String> trace) {
    for (int i = array.length / 2 - 1; i >= 0; i--) {
      trace.add("BUILD(" + i + ")");
      heapify(array, array.length, i, trace);
    }
  }

  private static void heapify(int[] array, int heapSize, int index, List<String> trace) {
    trace.add("HEAPIFY(" + heapSize + "," + index + ")");

    int largest = index;
    int left = 2 * index + 1;
    int right = 2 * index + 2;

    if (left < heapSize && array[left] > array[largest]) {
      trace.add("LEFT_GREATER(" + index + "->" + left + ")");
      largest = left;
    }

    if (right < heapSize && array[right] > array[largest]) {
      int previousLargest = largest;
      trace.add("RIGHT_GREATER(" + previousLargest + "->" + right + ")");
      largest = right;
    }

    if (largest != index) {
      trace.add("SWAP(" + index + "," + largest + ")");
      swap(array, index, largest);
      heapify(array, heapSize, largest, trace);
    }
  }

  private static void swap(int[] array, int i, int j) {
    int tmp = array[i];
    array[i] = array[j];
    array[j] = tmp;
  }

  public static final class SortResult {
    private final int[] sortedArray;
    private final List<String> trace;

    public SortResult(int[] sortedArray, List<String> trace) {
      this.sortedArray = Arrays.copyOf(sortedArray, sortedArray.length);
      this.trace = Collections.unmodifiableList(new ArrayList<>(trace));
    }

    public int[] getSortedArray() {
      return Arrays.copyOf(sortedArray, sortedArray.length);
    }

    public List<String> getTrace() {
      return trace;
    }
  }
}
