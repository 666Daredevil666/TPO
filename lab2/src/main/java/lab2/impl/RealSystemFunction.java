package lab2.impl;

import lab2.api.CotModule;
import lab2.api.CscModule;
import lab2.api.LnModule;
import lab2.api.Log10Module;
import lab2.api.SecModule;
import lab2.api.SinModule;
import lab2.api.SystemFunctionModule;
import lab2.variant.Variant320202;

public final class RealSystemFunction implements SystemFunctionModule {

  private static final double LN_DENOM_EPS = 1e-14;

  private final SinModule sin;
  private final CotModule cot;
  private final SecModule sec;
  private final CscModule csc;
  private final LnModule ln;
  private final Log10Module log10;

  public RealSystemFunction(
      SinModule sin,
      CotModule cot,
      SecModule sec,
      CscModule csc,
      LnModule ln,
      Log10Module log10) {
    this.sin = sin;
    this.cot = cot;
    this.sec = sec;
    this.csc = csc;
    this.ln = ln;
    this.log10 = log10;
  }

  @Override
  public double system(double x) {
    if (x <= 0.0) {
      return Variant320202.branchXLe0(
          sin.sin(x),
          cot.cot(x),
          sec.sec(x),
          csc.csc(x));
    }
    double lnX = ln.ln(x);
    if (Math.abs(lnX) < LN_DENOM_EPS) {
      throw new IllegalArgumentException("ln(x) too close to zero");
    }
    return Variant320202.branchXGt0(lnX, log10.log10(x));
  }
}
