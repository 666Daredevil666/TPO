package lab2.impl;

import lab2.api.LnModule;
import lab2.api.Log10Module;

public final class RealLog10Module implements Log10Module {

  private final LnModule ln;
  private final double ln10;

  public RealLog10Module(LnModule ln) {
    this.ln = ln;
    this.ln10 = ln.ln(10.0);
  }

  @Override
  public double log10(double x) {
    return ln.ln(x) / ln10;
  }
}

