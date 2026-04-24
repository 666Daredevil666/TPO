package lab2.benchmark;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

public final class LatencyReportGenerator {

  private static final List<String> PERCENTILES = List.of("p0.50", "p0.90", "p0.99");
  private static final int WIDTH = 960;
  private static final int HEIGHT = 540;
  private static final int LEFT = 92;
  private static final int RIGHT = 36;
  private static final int TOP = 52;
  private static final int BOTTOM = 84;
  private static final String[] COLORS = {"#2563eb", "#dc2626", "#16a34a", "#9333ea", "#ea580c"};

  private LatencyReportGenerator() {
  }

  public static void main(String[] args) throws IOException {
    if (args.length != 2) {
      throw new IllegalArgumentException("Usage: <jmh latency.csv> <output dir>");
    }
    generate(Path.of(args[0]), Path.of(args[1]));
  }

  public static void generate(Path inputCsv, Path outputDir) throws IOException {
    List<LatencyRow> rows = readRows(inputCsv);
    Files.createDirectories(outputDir);
    writeSummary(outputDir.resolve("latency-summary.csv"), rows);
    for (String percentile : PERCENTILES) {
      writeSvg(outputDir.resolve("latency-" + percentileName(percentile) + ".svg"), rows, percentile);
    }
  }

  static List<LatencyRow> readRows(Path inputCsv) throws IOException {
    try (BufferedReader reader = Files.newBufferedReader(inputCsv, StandardCharsets.UTF_8)) {
      String header = reader.readLine();
      if (header == null) {
        throw new IllegalArgumentException("JMH CSV is empty");
      }
      List<String> headers = parseCsvLine(header);
      int benchmarkIndex = headers.indexOf("Benchmark");
      int scoreIndex = headers.indexOf("Score");
      int epsilonIndex = headers.indexOf("Param: epsilon");
      int maxTermsIndex = headers.indexOf("Param: maxTerms");
      if (benchmarkIndex < 0 || scoreIndex < 0 || epsilonIndex < 0 || maxTermsIndex < 0) {
        throw new IllegalArgumentException("Unsupported JMH CSV header");
      }

      Map<Key, LatencyAccumulator> grouped = new TreeMap<>();
      String line;
      while ((line = reader.readLine()) != null) {
        List<String> values = parseCsvLine(line);
        if (values.size() <= Math.max(Math.max(benchmarkIndex, scoreIndex), Math.max(epsilonIndex, maxTermsIndex))) {
          continue;
        }
        String benchmark = values.get(benchmarkIndex);
        String percentile = percentileFromBenchmark(benchmark);
        if (percentile == null) {
          continue;
        }
        String epsilonLabel = values.get(epsilonIndex);
        int maxTerms = Integer.parseInt(values.get(maxTermsIndex));
        double score = parseJmhNumber(values.get(scoreIndex));
        Key key = new Key(epsilonLabel, maxTerms);
        grouped.computeIfAbsent(key, unused -> new LatencyAccumulator(epsilonLabel, maxTerms))
            .put(percentile, score);
      }
      return grouped.values().stream()
          .map(LatencyAccumulator::toRow)
          .filter(Objects::nonNull)
          .toList();
    }
  }

  private static List<String> parseCsvLine(String line) {
    List<String> values = new ArrayList<>();
    StringBuilder current = new StringBuilder();
    boolean quoted = false;
    for (int i = 0; i < line.length(); i++) {
      char ch = line.charAt(i);
      if (ch == '"') {
        if (quoted && i + 1 < line.length() && line.charAt(i + 1) == '"') {
          current.append('"');
          i++;
        } else {
          quoted = !quoted;
        }
      } else if (ch == ',' && !quoted) {
        values.add(current.toString());
        current.setLength(0);
      } else {
        current.append(ch);
      }
    }
    values.add(current.toString());
    return values;
  }

  private static String percentileFromBenchmark(String benchmark) {
    for (String percentile : PERCENTILES) {
      if (benchmark.endsWith("·" + percentile)) {
        return percentile;
      }
    }
    return null;
  }

  private static double parseJmhNumber(String raw) {
    if ("NaN".equals(raw)) {
      return Double.NaN;
    }
    return Double.parseDouble(raw.replace(',', '.'));
  }

  private static void writeSummary(Path path, List<LatencyRow> rows) throws IOException {
    try (BufferedWriter writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8)) {
      writer.write("epsilon;maxTerms;medianNs;p90Ns;p99Ns");
      writer.newLine();
      for (LatencyRow row : rows) {
        writer.write(row.epsilonLabel());
        writer.write(';');
        writer.write(Integer.toString(row.maxTerms()));
        writer.write(';');
        writer.write(format(row.medianNs()));
        writer.write(';');
        writer.write(format(row.p90Ns()));
        writer.write(';');
        writer.write(format(row.p99Ns()));
        writer.newLine();
      }
    }
  }

  private static void writeSvg(Path path, List<LatencyRow> rows, String percentile) throws IOException {
    List<String> epsilons = rows.stream()
        .map(LatencyRow::epsilonLabel)
        .distinct()
        .sorted(Comparator.comparingDouble(LatencyReportGenerator::parseEpsilon).reversed())
        .toList();
    List<Integer> maxTerms = rows.stream()
        .map(LatencyRow::maxTerms)
        .distinct()
        .sorted()
        .toList();
    double maxValue = rows.stream()
        .mapToDouble(row -> row.value(percentile))
        .max()
        .orElse(1.0);
    double yMax = niceUpperBound(maxValue);

    StringBuilder svg = new StringBuilder();
    svg.append("<svg xmlns=\"http://www.w3.org/2000/svg\" width=\"")
        .append(WIDTH)
        .append("\" height=\"")
        .append(HEIGHT)
        .append("\" viewBox=\"0 0 ")
        .append(WIDTH)
        .append(' ')
        .append(HEIGHT)
        .append("\">\n");
    svg.append("<rect width=\"100%\" height=\"100%\" fill=\"#ffffff\"/>\n");
    svg.append("<text x=\"")
        .append(LEFT)
        .append("\" y=\"30\" font-family=\"Arial\" font-size=\"22\" font-weight=\"700\">")
        .append(titleFor(percentile))
        .append(" latency by precision</text>\n");
    drawAxes(svg, epsilons, yMax);

    Map<String, LatencyRow> byKey = new LinkedHashMap<>();
    for (LatencyRow row : rows) {
      byKey.put(row.epsilonLabel() + ":" + row.maxTerms(), row);
    }
    for (int i = 0; i < maxTerms.size(); i++) {
      int terms = maxTerms.get(i);
      String color = COLORS[i % COLORS.length];
      List<Point> points = new ArrayList<>();
      for (int e = 0; e < epsilons.size(); e++) {
        LatencyRow row = byKey.get(epsilons.get(e) + ":" + terms);
        if (row != null) {
          points.add(new Point(xFor(e, epsilons.size()), yFor(row.value(percentile), yMax)));
        }
      }
      drawSeries(svg, points, color, terms, i);
    }
    svg.append("</svg>\n");
    Files.writeString(path, svg.toString(), StandardCharsets.UTF_8);
  }

  private static void drawAxes(StringBuilder svg, List<String> epsilons, double yMax) {
    int chartRight = WIDTH - RIGHT;
    int chartBottom = HEIGHT - BOTTOM;
    svg.append("<line x1=\"").append(LEFT).append("\" y1=\"").append(TOP)
        .append("\" x2=\"").append(LEFT).append("\" y2=\"").append(chartBottom)
        .append("\" stroke=\"#111827\" stroke-width=\"1\"/>\n");
    svg.append("<line x1=\"").append(LEFT).append("\" y1=\"").append(chartBottom)
        .append("\" x2=\"").append(chartRight).append("\" y2=\"").append(chartBottom)
        .append("\" stroke=\"#111827\" stroke-width=\"1\"/>\n");
    for (int i = 0; i <= 5; i++) {
      double value = yMax * i / 5.0;
      double y = yFor(value, yMax);
      svg.append("<line x1=\"").append(LEFT).append("\" y1=\"").append(format(y))
          .append("\" x2=\"").append(chartRight).append("\" y2=\"").append(format(y))
          .append("\" stroke=\"#e5e7eb\"/>\n");
      svg.append("<text x=\"").append(LEFT - 12).append("\" y=\"").append(format(y + 4))
          .append("\" text-anchor=\"end\" font-family=\"Arial\" font-size=\"12\" fill=\"#374151\">")
          .append(format(value)).append("</text>\n");
    }
    for (int i = 0; i < epsilons.size(); i++) {
      double x = xFor(i, epsilons.size());
      svg.append("<text x=\"").append(format(x)).append("\" y=\"").append(chartBottom + 28)
          .append("\" text-anchor=\"middle\" font-family=\"Arial\" font-size=\"13\" fill=\"#374151\">")
          .append(epsilons.get(i)).append("</text>\n");
    }
    svg.append("<text x=\"").append((LEFT + chartRight) / 2).append("\" y=\"").append(HEIGHT - 24)
        .append("\" text-anchor=\"middle\" font-family=\"Arial\" font-size=\"14\" fill=\"#111827\">epsilon</text>\n");
    svg.append("<text transform=\"translate(24 ").append((TOP + chartBottom) / 2)
        .append(") rotate(-90)\" text-anchor=\"middle\" font-family=\"Arial\" font-size=\"14\" fill=\"#111827\">ns/op</text>\n");
  }

  private static void drawSeries(StringBuilder svg, List<Point> points, String color, int maxTerms, int index) {
    if (points.isEmpty()) {
      return;
    }
    svg.append("<polyline fill=\"none\" stroke=\"").append(color)
        .append("\" stroke-width=\"2.5\" points=\"");
    for (Point point : points) {
      svg.append(format(point.x())).append(',').append(format(point.y())).append(' ');
    }
    svg.append("\"/>\n");
    for (Point point : points) {
      svg.append("<circle cx=\"").append(format(point.x())).append("\" cy=\"").append(format(point.y()))
          .append("\" r=\"4\" fill=\"").append(color).append("\"/>\n");
    }
    int legendX = WIDTH - 188;
    int legendY = TOP + 20 + index * 24;
    svg.append("<line x1=\"").append(legendX).append("\" y1=\"").append(legendY)
        .append("\" x2=\"").append(legendX + 28).append("\" y2=\"").append(legendY)
        .append("\" stroke=\"").append(color).append("\" stroke-width=\"3\"/>\n");
    svg.append("<text x=\"").append(legendX + 38).append("\" y=\"").append(legendY + 5)
        .append("\" font-family=\"Arial\" font-size=\"13\" fill=\"#111827\">maxTerms=")
        .append(maxTerms).append("</text>\n");
  }

  private static double xFor(int index, int count) {
    if (count <= 1) {
      return (LEFT + WIDTH - RIGHT) / 2.0;
    }
    return LEFT + index * ((WIDTH - RIGHT - LEFT) / (double) (count - 1));
  }

  private static double yFor(double value, double yMax) {
    double chartHeight = HEIGHT - BOTTOM - TOP;
    return HEIGHT - BOTTOM - (value / yMax) * chartHeight;
  }

  private static double niceUpperBound(double value) {
    if (value <= 0.0 || !Double.isFinite(value)) {
      return 1.0;
    }
    double power = Math.pow(10.0, Math.floor(Math.log10(value)));
    double scaled = value / power;
    double nice = scaled <= 2.0 ? 2.0 : scaled <= 5.0 ? 5.0 : 10.0;
    return nice * power;
  }

  private static double parseEpsilon(String label) {
    return Double.parseDouble(label);
  }

  private static String percentileName(String percentile) {
    return switch (percentile) {
      case "p0.50" -> "median";
      case "p0.90" -> "p90";
      case "p0.99" -> "p99";
      default -> percentile.replace('.', '_');
    };
  }

  private static String titleFor(String percentile) {
    return switch (percentile) {
      case "p0.50" -> "Median";
      case "p0.90" -> "P90";
      case "p0.99" -> "P99";
      default -> percentile;
    };
  }

  private static String format(double value) {
    return String.format(Locale.US, "%.3f", value);
  }

  private record Key(String epsilonLabel, int maxTerms) implements Comparable<Key> {
    @Override
    public int compareTo(Key other) {
      int epsilonCompare = Double.compare(parseEpsilon(other.epsilonLabel), parseEpsilon(epsilonLabel));
      if (epsilonCompare != 0) {
        return epsilonCompare;
      }
      return Integer.compare(maxTerms, other.maxTerms);
    }
  }

  private static final class LatencyAccumulator {
    private final String epsilonLabel;
    private final int maxTerms;
    private final Map<String, Double> values = new LinkedHashMap<>();

    private LatencyAccumulator(String epsilonLabel, int maxTerms) {
      this.epsilonLabel = epsilonLabel;
      this.maxTerms = maxTerms;
    }

    private void put(String percentile, double score) {
      values.put(percentile, score);
    }

    private LatencyRow toRow() {
      if (!values.keySet().containsAll(PERCENTILES)) {
        return null;
      }
      return new LatencyRow(
          epsilonLabel,
          maxTerms,
          values.get("p0.50"),
          values.get("p0.90"),
          values.get("p0.99"));
    }
  }

  record LatencyRow(String epsilonLabel, int maxTerms, double medianNs, double p90Ns, double p99Ns) {
    private double value(String percentile) {
      return switch (percentile) {
        case "p0.50" -> medianNs;
        case "p0.90" -> p90Ns;
        case "p0.99" -> p99Ns;
        default -> throw new IllegalArgumentException(percentile);
      };
    }
  }

  private record Point(double x, double y) {
  }
}
