package lab2.csv;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.DoubleUnaryOperator;

public final class CsvExporter {

  private CsvExporter() {
  }

  public static void export(
      Path path,
      double x0,
      double x1,
      double step,
      DoubleUnaryOperator module,
      char delimiter)
      throws IOException {
    if (step <= 0.0 || !Double.isFinite(step)) {
      throw new IllegalArgumentException("step must be positive and finite");
    }
    try (BufferedWriter w = Files.newBufferedWriter(path, StandardCharsets.UTF_8)) {
      w.write("X");
      w.write(delimiter);
      w.write("Y");
      w.newLine();
      if (x1 < x0) {
        throw new IllegalArgumentException("x1 must be >= x0");
      }
      double x = x0;
      while (x <= x1 + 1e-12) {
        double xi = Math.min(x, x1);
        double y;
        try {
          y = module.applyAsDouble(xi);
        } catch (RuntimeException ex) {
          y = Double.NaN;
        }
        w.write(Double.toString(xi));
        w.write(delimiter);
        w.write(Double.toString(y));
        w.newLine();
        if (xi >= x1 - 1e-12) {
          break;
        }
        x += step;
      }
    }
  }
}
