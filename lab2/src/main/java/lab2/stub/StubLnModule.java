package lab2.stub;

import lab2.api.LnModule;

public final class StubLnModule implements LnModule {

  private final Table1D table;

  public StubLnModule(Table1D table) {
    this.table = table;
  }

  @Override
  public double ln(double x) {
    return table.valueAt(x);
  }
}
