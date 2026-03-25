package lab1.task1;

import lab1.task1.utils.Trigonometric;

public class App {
  public static void main(String[] args) {
    double[] values = {0.0, Math.PI / 3, Math.PI / 2, Math.PI, 10.0};
    for (double value : values) {
      System.out.printf("cos(%f) = %.12f%n", value, Trigonometric.cos(value));
    }
  }
}
