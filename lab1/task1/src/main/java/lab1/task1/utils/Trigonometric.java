package lab1.task1.utils;

public final class Trigonometric {

  private static final double TWO_PI = 2.0 * Math.PI;
  private static final double EPS = 1e-16;
  private static final int DEFAULT_MAX_TERMS = 100;

  private Trigonometric() {
  }

  public static double cos(double x) {
    return cos(x, DEFAULT_MAX_TERMS);
  }

  /**
   * Вычисляет cos(x) по разложению в степенной ряд Маклорена.
   * Перед суммированием аргумент приводится к диапазону [-pi; pi],
   * чтобы ряд сходился быстрее и устойчивее для больших значений x.
   */

  public static double cos(double x, int maxTerms) {
    if (Double.isNaN(x) || Double.isInfinite(x)) {
      return Double.NaN;
    }
    if (maxTerms <= 0) {
      throw new IllegalArgumentException("maxTerms must be positive");
    }

    double reduced = Math.IEEEremainder(x, TWO_PI);
    if (reduced == 0.0) {
      return 1.0;
    }

    double term = 1.0;
    double sum = 1.0;

    for (int n = 1; n <= maxTerms; n++) {
      term *= -(reduced * reduced) / ((2.0 * n - 1.0) * (2.0 * n));
      sum += term;

      if (Math.abs(term) < EPS) {
        break;
      }
    }

    return sum;
  }
}
