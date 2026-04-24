package lab2.series;

public final class SinSeries {

  private static final double TWO_PI = 2.0 * Math.PI;

  private SinSeries() {
  }

  public static double sin(double x, double epsilon, int maxTerms) {
    if (!Double.isFinite(x)) {
      throw new IllegalArgumentException("x must be finite");
    }
    double y = Math.IEEEremainder(x, TWO_PI);
    if (y == 0.0) {
      return 0.0;
    }
    double term = y;
    double sum = term;
    for (int n = 1; n < maxTerms; n++) {
      term *= -(y * y) / ((2.0 * n) * (2.0 * n + 1.0));
      sum += term;
      if (Math.abs(term) < epsilon) {
        break;
      }
    }
    return sum;
  }
}
