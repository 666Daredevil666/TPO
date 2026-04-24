package lab2.impl;

import lab2.api.CosModule;
import lab2.api.SinModule;
import lab2.api.TgModule;

public final class RealTgModule implements TgModule {

  private static final double COS_EPS = 1e-14;

  private final SinModule sin;
  private final CosModule cos;

  public RealTgModule(SinModule sin, CosModule cos) {
    this.sin = sin;
    this.cos = cos;
  }

  @Override
  public double tg(double x) {
    double c = cos.cos(x);
    if (Math.abs(c) < COS_EPS) {
      throw new IllegalArgumentException("cos(x) too close to zero");
    }
    return sin.sin(x) / c;
  }
}
