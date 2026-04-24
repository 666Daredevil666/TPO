package lab2.stub;

import lab2.api.TgModule;

public final class StubTgModule implements TgModule {

  private final Table1D table;

  public StubTgModule(Table1D table) {
    this.table = table;
  }

  @Override
  public double tg(double x) {
    return table.valueAt(x);
  }
}
