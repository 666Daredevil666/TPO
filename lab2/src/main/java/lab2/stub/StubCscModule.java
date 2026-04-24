package lab2.stub;

import lab2.api.CscModule;

public final class StubCscModule implements CscModule {

  private final Table1D table;

  public StubCscModule(Table1D table) {
    this.table = table;
  }

  @Override
  public double csc(double x) {
    return table.valueAt(x);
  }
}

