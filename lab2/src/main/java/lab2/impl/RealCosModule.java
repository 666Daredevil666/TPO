package lab2.impl;

import lab2.api.CosModule;
import lab2.api.SinModule;

public final class RealCosModule implements CosModule {

  private final SinModule sin;

  public RealCosModule(SinModule sin) {
    this.sin = sin;
  }

  @Override
  public double cos(double x) {
    return sin.sin(x + Math.PI / 2.0);
  }
}
