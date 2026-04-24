package lab2.stub;

import lab2.api.CosModule;

public final class StubCosModule implements CosModule {

  private final Table1D table;

  public StubCosModule(Table1D table) {
    this.table = table;
  }

  @Override
  public double cos(double x) {
    return table.valueAt(x);
  }
}
