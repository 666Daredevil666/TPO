package lab2.impl;

import lab2.api.CosModule;
import lab2.api.CotModule;
import lab2.api.SinModule;

public final class RealCotModule implements CotModule {

  private static final double SIN_EPS = 1e-14;

  private final SinModule sin;
  private final CosModule cos;

  public RealCotModule(SinModule sin, CosModule cos) {
    this.sin = sin;
    this.cos = cos;
  }

  @Override
  public double cot(double x) {
    double s = sin.sin(x);
    if (Math.abs(s) < SIN_EPS) {
      throw new IllegalArgumentException("sin(x) too close to zero");
    }
    return cos.cos(x) / s;
  }
}

