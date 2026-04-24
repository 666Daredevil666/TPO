package lab2.variant;

public final class Variant320202 {

  public static final int ID = 320202;

  public static double branchXLe0(double sinX, double cotX, double secX, double cscX) {
    double a = sinX * cotX;
    double b = (a * a) / (secX / cscX);
    return b + cotX;
  }

  public static double branchXGt0(double lnX, double log10X) {
    double t = (lnX / lnX) - lnX;
    double u = (t * t * t) - log10X;
    return u * u;
  }

  private Variant320202() {
  }
}
