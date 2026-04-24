package lab2;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import lab2.series.LnSeries;
import lab2.series.SinSeries;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Базовые функции, реализованные через ряды")
class SeriesTest {

  @Test
  @DisplayName("sin(x) совпадает с эталонной реализацией в обычных точках")
  void sin_matches_reference() {
    // Проверяем базовую тригонометрическую функцию на нуле, положительном,
    // отрицательном и большем аргументе с редукцией периода.
    double eps = 1e-12;
    int max = 20000;
    assertEquals(Math.sin(0.0), SinSeries.sin(0.0, eps, max), 1e-15);
    assertEquals(Math.sin(0.3), SinSeries.sin(0.3, eps, max), 1e-10);
    assertEquals(Math.sin(-1.7), SinSeries.sin(-1.7, eps, max), 1e-10);
    assertEquals(Math.sin(12.3), SinSeries.sin(12.3, eps, max), 1e-9);
  }

  @Test
  @DisplayName("sin(x) отклоняет NaN и бесконечности")
  void sin_rejects_non_finite() {
    // Нечисловые аргументы не входят в область допустимых входов ряда.
    assertThrows(IllegalArgumentException.class, () -> SinSeries.sin(Double.NaN, 1e-9, 100));
  }

  @Test
  @DisplayName("ln(x) совпадает с эталонной реализацией в разных областях x > 0")
  void ln_matches_reference() {
    // Покрываем значения меньше 1, ровно 1, около 2 и большое положительное x.
    double eps = 1e-11;
    int max = 50000;
    assertEquals(Math.log(0.2), LnSeries.ln(0.2, eps, max), 1e-8);
    assertEquals(Math.log(1.0), LnSeries.ln(1.0, eps, max), 1e-12);
    assertEquals(Math.log(2.0), LnSeries.ln(2.0, eps, max), 1e-8);
    assertEquals(Math.log(100.0), LnSeries.ln(100.0, eps, max), 1e-7);
  }

  @Test
  @DisplayName("ln(x) отклоняет x <= 0")
  void ln_rejects_non_positive() {
    // Натуральный логарифм определён только для положительных аргументов.
    assertThrows(IllegalArgumentException.class, () -> LnSeries.ln(0.0, 1e-9, 100));
    assertThrows(IllegalArgumentException.class, () -> LnSeries.ln(-1.0, 1e-9, 100));
  }
}
