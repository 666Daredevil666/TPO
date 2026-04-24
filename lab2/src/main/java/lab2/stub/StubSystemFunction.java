package lab2.stub;

import lab2.api.SystemFunctionModule;

public final class StubSystemFunction implements SystemFunctionModule {

  private final Table1D table;

  public StubSystemFunction(Table1D table) {
    this.table = table;
  }

  @Override
  public double system(double x) {
    return table.valueAt(x);
  }
}
