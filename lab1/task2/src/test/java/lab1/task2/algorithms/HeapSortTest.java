package lab1.task2.algorithms;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class HeapSortTest {

  @Test
  @DisplayName("Пустой массив")
  void testEmptyArray() {
    HeapSort.SortResult result = HeapSort.sortWithTrace(new int[]{});

    assertArrayEquals(new int[]{}, result.getSortedArray());
    assertEquals(List.of("START", "END"), result.getTrace());
  }

  @Test
  @DisplayName("Массив из одного элемента")
  void testSingleElement() {
    HeapSort.SortResult result = HeapSort.sortWithTrace(new int[]{5});

    assertArrayEquals(new int[]{5}, result.getSortedArray());
    assertEquals(List.of("START", "END"), result.getTrace());
  }

  @Test
  @DisplayName("Два элемента")
  void testTwoElements() {
    HeapSort.SortResult result = HeapSort.sortWithTrace(new int[]{1, 2});

    assertArrayEquals(new int[]{1, 2}, result.getSortedArray());
    assertEquals(List.of(
      "START",
      "BUILD(0)",
      "HEAPIFY(2,0)",
      "LEFT_GREATER(0->1)",
      "SWAP(0,1)",
      "HEAPIFY(2,1)",
      "EXTRACT(1)",
      "SWAP(0,1)",
      "HEAPIFY(1,0)",
      "END"
    ), result.getTrace());
  }

  @Test
  @DisplayName("Уже отсортированный массив")
  void testAscendingArray() {
    HeapSort.SortResult result = HeapSort.sortWithTrace(new int[]{1, 2, 3, 4});

    assertArrayEquals(new int[]{1, 2, 3, 4}, result.getSortedArray());
    assertEquals(List.of(
      "START",
      "BUILD(1)",
      "HEAPIFY(4,1)",
      "LEFT_GREATER(1->3)",
      "SWAP(1,3)",
      "HEAPIFY(4,3)",
      "BUILD(0)",
      "HEAPIFY(4,0)",
      "LEFT_GREATER(0->1)",
      "SWAP(0,1)",
      "HEAPIFY(4,1)",
      "LEFT_GREATER(1->3)",
      "SWAP(1,3)",
      "HEAPIFY(4,3)",
      "EXTRACT(3)",
      "SWAP(0,3)",
      "HEAPIFY(3,0)",
      "LEFT_GREATER(0->1)",
      "RIGHT_GREATER(1->2)",
      "SWAP(0,2)",
      "HEAPIFY(3,2)",
      "EXTRACT(2)",
      "SWAP(0,2)",
      "HEAPIFY(2,0)",
      "LEFT_GREATER(0->1)",
      "SWAP(0,1)",
      "HEAPIFY(2,1)",
      "EXTRACT(1)",
      "SWAP(0,1)",
      "HEAPIFY(1,0)",
      "END"
    ), result.getTrace());
  }

  @Test
  @DisplayName("Обратно отсортированный массив")
  void testDescendingArray() {
    HeapSort.SortResult result = HeapSort.sortWithTrace(new int[]{4, 3, 2, 1});

    assertArrayEquals(new int[]{1, 2, 3, 4}, result.getSortedArray());
    assertEquals(List.of(
      "START",
      "BUILD(1)",
      "HEAPIFY(4,1)",
      "BUILD(0)",
      "HEAPIFY(4,0)",
      "EXTRACT(3)",
      "SWAP(0,3)",
      "HEAPIFY(3,0)",
      "LEFT_GREATER(0->1)",
      "SWAP(0,1)",
      "HEAPIFY(3,1)",
      "EXTRACT(2)",
      "SWAP(0,2)",
      "HEAPIFY(2,0)",
      "EXTRACT(1)",
      "SWAP(0,1)",
      "HEAPIFY(1,0)",
      "END"
    ), result.getTrace());
  }

  @Test
  @DisplayName("Массив с повторяющимися значениями")
  void testDuplicateValues() {
    HeapSort.SortResult result = HeapSort.sortWithTrace(new int[]{2, 2, 1, 2});

    assertArrayEquals(new int[]{1, 2, 2, 2}, result.getSortedArray());
    assertEquals(List.of(
      "START",
      "BUILD(1)",
      "HEAPIFY(4,1)",
      "BUILD(0)",
      "HEAPIFY(4,0)",
      "EXTRACT(3)",
      "SWAP(0,3)",
      "HEAPIFY(3,0)",
      "EXTRACT(2)",
      "SWAP(0,2)",
      "HEAPIFY(2,0)",
      "LEFT_GREATER(0->1)",
      "SWAP(0,1)",
      "HEAPIFY(2,1)",
      "EXTRACT(1)",
      "SWAP(0,1)",
      "HEAPIFY(1,0)",
      "END"
    ), result.getTrace());
  }

  @Test
  @DisplayName("Массив с отрицательными числами")
  void testNegativeValues() {
    HeapSort.SortResult result = HeapSort.sortWithTrace(new int[]{0, -1, 5, -3});

    assertArrayEquals(new int[]{-3, -1, 0, 5}, result.getSortedArray());
    assertEquals(List.of(
      "START",
      "BUILD(1)",
      "HEAPIFY(4,1)",
      "BUILD(0)",
      "HEAPIFY(4,0)",
      "RIGHT_GREATER(0->2)",
      "SWAP(0,2)",
      "HEAPIFY(4,2)",
      "EXTRACT(3)",
      "SWAP(0,3)",
      "HEAPIFY(3,0)",
      "LEFT_GREATER(0->1)",
      "RIGHT_GREATER(1->2)",
      "SWAP(0,2)",
      "HEAPIFY(3,2)",
      "EXTRACT(2)",
      "SWAP(0,2)",
      "HEAPIFY(2,0)",
      "LEFT_GREATER(0->1)",
      "SWAP(0,1)",
      "HEAPIFY(2,1)",
      "EXTRACT(1)",
      "SWAP(0,1)",
      "HEAPIFY(1,0)",
      "END"
    ), result.getTrace());
  }
}
