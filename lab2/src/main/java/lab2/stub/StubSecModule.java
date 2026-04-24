package lab2.stub;

import lab2.api.SecModule;

public final class StubSecModule implements SecModule {

  private final Table1D table;

  public StubSecModule(Table1D table) {
    this.table = table;
  }

  @Override
  public double sec(double x) {
    return table.valueAt(x);
  }
}

