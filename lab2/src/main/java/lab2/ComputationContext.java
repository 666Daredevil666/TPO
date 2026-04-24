package lab2;

public final class ComputationContext {

  private final double epsilon;
  private final int maxSeriesTerms;

  public ComputationContext(double epsilon, int maxSeriesTerms) {
    if (epsilon <= 0.0 || !Double.isFinite(epsilon)) {
      throw new IllegalArgumentException("epsilon must be positive and finite");
    }
    if (maxSeriesTerms <= 0) {
      throw new IllegalArgumentException("maxSeriesTerms must be positive");
    }
    this.epsilon = epsilon;
    this.maxSeriesTerms = maxSeriesTerms;
  }

  public double epsilon() {
    return epsilon;
  }

  public int maxSeriesTerms() {
    return maxSeriesTerms;
  }
}
