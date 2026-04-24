package lab2.stub;

import lab2.api.Log2Module;

@Deprecated
public final class StubLog2Module implements Log2Module {

  private final Table1D table;

  public StubLog2Module(Table1D table) {
    this.table = table;
  }

  @Override
  public double log2(double x) {
    return table.valueAt(x);
  }
}
