package lab2.impl;

import lab2.api.CosModule;
import lab2.api.SecModule;

public final class RealSecModule implements SecModule {

  private static final double COS_EPS = 1e-14;

  private final CosModule cos;

  public RealSecModule(CosModule cos) {
    this.cos = cos;
  }

  @Override
  public double sec(double x) {
    double c = cos.cos(x);
    if (Math.abs(c) < COS_EPS) {
      throw new IllegalArgumentException("cos(x) too close to zero");
    }
    return 1.0 / c;
  }
}

