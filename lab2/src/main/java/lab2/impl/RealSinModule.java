package lab2.impl;

import lab2.ComputationContext;
import lab2.api.SinModule;
import lab2.series.SinSeries;

public final class RealSinModule implements SinModule {

  private final ComputationContext context;

  public RealSinModule(ComputationContext context) {
    this.context = context;
  }

  @Override
  public double sin(double x) {
    return SinSeries.sin(x, context.epsilon(), context.maxSeriesTerms());
  }
}
