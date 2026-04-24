package lab2.stub;

import lab2.api.SinModule;

public final class StubSinModule implements SinModule {

  private final Table1D table;

  public StubSinModule(Table1D table) {
    this.table = table;
  }

  @Override
  public double sin(double x) {
    return table.valueAt(x);
  }
}
