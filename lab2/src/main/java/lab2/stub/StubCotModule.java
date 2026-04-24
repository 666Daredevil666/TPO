package lab2.stub;

import lab2.api.CotModule;

public final class StubCotModule implements CotModule {

  private final Table1D table;

  public StubCotModule(Table1D table) {
    this.table = table;
  }

  @Override
  public double cot(double x) {
    return table.valueAt(x);
  }
}

