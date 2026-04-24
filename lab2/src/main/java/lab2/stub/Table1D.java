package lab2.stub;

import java.util.Arrays;

public final class Table1D {

  private final double[] xs;
  private final double[] ys;

  public Table1D(double[] xs, double[] ys) {
    if (xs.length != ys.length) {
      throw new IllegalArgumentException("xs and ys length mismatch");
    }
    if (xs.length == 0) {
      throw new IllegalArgumentException("table must not be empty");
    }
    Integer[] order = new Integer[xs.length];
    for (int i = 0; i < order.length; i++) {
      order[i] = i;
    }
    Arrays.sort(order, (a, b) -> Double.compare(xs[a], xs[b]));
    this.xs = new double[xs.length];
    this.ys = new double[ys.length];
    for (int i = 0; i < order.length; i++) {
      this.xs[i] = xs[order[i]];
      this.ys[i] = ys[order[i]];
    }
  }

  public double valueAt(double x) {
    if (x <= xs[0]) {
      return extrapolateLeft(x);
    }
    if (x >= xs[xs.length - 1]) {
      return extrapolateRight(x);
    }
    int i = Arrays.binarySearch(xs, x);
    if (i >= 0) {
      return ys[i];
    }
    int ins = -i - 1;
    return interpolate(x, ins - 1, ins);
  }

  private double extrapolateLeft(double x) {
    if (xs.length == 1) {
      return ys[0];
    }
    return interpolate(x, 0, 1);
  }

  private double extrapolateRight(double x) {
    if (xs.length == 1) {
      return ys[0];
    }
    return interpolate(x, xs.length - 2, xs.length - 1);
  }

  private double interpolate(double x, int i0, int i1) {
    double x0 = xs[i0];
    double x1 = xs[i1];
    if (x1 == x0) {
      return ys[i0];
    }
    double t = (x - x0) / (x1 - x0);
    return ys[i0] + t * (ys[i1] - ys[i0]);
  }
}
