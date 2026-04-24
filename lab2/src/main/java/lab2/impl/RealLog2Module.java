package lab2.impl;

import lab2.api.LnModule;
import lab2.api.Log2Module;

@Deprecated
public final class RealLog2Module implements Log2Module {

  private final LnModule ln;
  private final double ln2;

  public RealLog2Module(LnModule ln) {
    this.ln = ln;
    this.ln2 = ln.ln(2.0);
  }

  @Override
  public double log2(double x) {
    return ln.ln(x) / ln2;
  }
}
