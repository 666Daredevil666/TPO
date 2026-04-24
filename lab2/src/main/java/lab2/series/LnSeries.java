package lab2.series;

public final class LnSeries {

  private LnSeries() {
  }

  public static double ln(double x, double epsilon, int maxTerms) {
    if (x <= 0.0 || !Double.isFinite(x)) {
      throw new IllegalArgumentException("x must be finite and positive");
    }
    int k = 0;
    double y = x;
    while (y > 2.0) {
      y /= 2.0;
      k++;
    }
    while (y < 0.5) {
      y *= 2.0;
      k--;
    }
    double ln2 = ln2FromSplit(epsilon, maxTerms);
    double lnY;
    if (Math.abs(y - 2.0) < 1e-14) {
      lnY = ln2;
    } else {
      lnY = lnReduced(y, epsilon, maxTerms);
    }
    return lnY + k * ln2;
  }

  private static double ln2FromSplit(double epsilon, int maxTerms) {
    return lnReduced(4.0 / 3.0, epsilon, maxTerms) + lnReduced(1.5, epsilon, maxTerms);
  }

  private static double lnReduced(double y, double epsilon, int maxTerms) {
    double u = y - 1.0;
    double term = u;
    double sum = term;
    for (int n = 2; n <= maxTerms; n++) {
      term *= -u * (n - 1.0) / n;
      sum += term;
      if (Math.abs(term) < epsilon) {
        break;
      }
    }
    return sum;
  }
}
