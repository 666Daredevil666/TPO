package lab1.task1.utils;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class TrigonometricTest {

  static Stream<Double> cornerCases() {
    return Stream.of(
      -2 * Math.PI,
      -Math.PI,
      -Math.PI / 2,
      -Math.PI / 3,
      -1.0,
      -1e-6,
      0.0,
      1e-6,
      1.0,
      Math.PI / 3,
      Math.PI / 2,
      Math.PI,
      2 * Math.PI,
      100.0,
      -100.0,
      Double.MIN_VALUE,
      Double.NaN,
      Double.POSITIVE_INFINITY,
      Double.NEGATIVE_INFINITY
    );
  }

  @ParameterizedTest(name = "cos({0})")
  @MethodSource("cornerCases")
  @DisplayName("Проверка характерных точек")
  void checkCornerDots(double value) {
    double actual = Trigonometric.cos(value);
    double expected = Math.cos(value);

    if (Double.isNaN(expected)) {
      assertTrue(Double.isNaN(actual));
    } else {
      assertEquals(expected, actual, 1e-10);
    }
  }

  @ParameterizedTest(name = "cos({0}) = {1}")
  @DisplayName("Проверка табличных значений")
  @CsvFileSource(resources = "/table_values.csv", numLinesToSkip = 1, delimiter = ';')
  void checkTableValues(double x, double y) {
    assertEquals(y, Trigonometric.cos(x), 1e-10);
  }

  @Test
  @DisplayName("Проверка точек на промежутке [-2pi; 2pi] с шагом 0.1")
  void checkRange() {
    for (double x = -2 * Math.PI; x <= 2 * Math.PI; x += 0.1) {
      assertEquals(Math.cos(x), Trigonometric.cos(x), 1e-10);
    }
  }

  @Test
  @DisplayName("Fuzzy testing")
  void checkRandomDots() {
    for (int i = 0; i < 100_000; i++) {
      double randomValue = ThreadLocalRandom.current().nextDouble(-1_000.0, 1_000.0);
      assertEquals(Math.cos(randomValue), Trigonometric.cos(randomValue), 1e-10);
    }
  }

  @Test
  @DisplayName("Проверка некорректного числа членов ряда")
  void checkInvalidMaxTerms() {
    assertThrows(IllegalArgumentException.class, () -> Trigonometric.cos(1.0, 0));
  }
}
