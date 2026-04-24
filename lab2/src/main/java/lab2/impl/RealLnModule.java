package lab2.impl;

import lab2.ComputationContext;
import lab2.api.LnModule;
import lab2.series.LnSeries;

public final class RealLnModule implements LnModule {

  private final ComputationContext context;

  public RealLnModule(ComputationContext context) {
    this.context = context;
  }

  @Override
  public double ln(double x) {
    return LnSeries.ln(x, context.epsilon(), context.maxSeriesTerms());
  }
}
