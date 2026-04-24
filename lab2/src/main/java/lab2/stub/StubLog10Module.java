package lab2.stub;

import lab2.api.Log10Module;

public final class StubLog10Module implements Log10Module {

  private final Table1D table;

  public StubLog10Module(Table1D table) {
    this.table = table;
  }

  @Override
  public double log10(double x) {
    return table.valueAt(x);
  }
}

