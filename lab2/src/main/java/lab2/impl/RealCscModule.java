package lab2.impl;

import lab2.api.CscModule;
import lab2.api.SinModule;

public final class RealCscModule implements CscModule {

  private static final double SIN_EPS = 1e-14;

  private final SinModule sin;

  public RealCscModule(SinModule sin) {
    this.sin = sin;
  }

  @Override
  public double csc(double x) {
    double s = sin.sin(x);
    if (Math.abs(s) < SIN_EPS) {
      throw new IllegalArgumentException("sin(x) too close to zero");
    }
    return 1.0 / s;
  }
}

