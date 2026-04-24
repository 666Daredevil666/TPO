package lab2.benchmark;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class LatencyReportGeneratorTest {

  @TempDir
  Path tempDir;

  @Test
  void generator_extracts_percentiles_and_writes_report_files() throws Exception {
    Path input = tempDir.resolve("latency.csv");
    Files.write(
        input,
        List.of(
            "\"Benchmark\",\"Mode\",\"Threads\",\"Samples\",\"Score\",\"Score Error (99,9%)\",\"Unit\",\"Param: epsilon\",\"Param: maxTerms\"",
            "\"lab2.jmh.SystemFunctionLatencyBenchmark.sampleSystem\",\"sample\",1,10,\"100,000000\",\"1,0\",\"ns/op\",1e-4,500",
            "\"lab2.jmh.SystemFunctionLatencyBenchmark.sampleSystem:sampleSystem·p0.50\",\"sample\",1,1,\"42,000000\",NaN,\"ns/op\",1e-4,500",
            "\"lab2.jmh.SystemFunctionLatencyBenchmark.sampleSystem:sampleSystem·p0.90\",\"sample\",1,1,\"84,000000\",NaN,\"ns/op\",1e-4,500",
            "\"lab2.jmh.SystemFunctionLatencyBenchmark.sampleSystem:sampleSystem·p0.99\",\"sample\",1,1,\"125,000000\",NaN,\"ns/op\",1e-4,500",
            "\"lab2.jmh.SystemFunctionLatencyBenchmark.sampleSystem:sampleSystem·p0.50\",\"sample\",1,1,\"166,000000\",NaN,\"ns/op\",1e-8,500",
            "\"lab2.jmh.SystemFunctionLatencyBenchmark.sampleSystem:sampleSystem·p0.90\",\"sample\",1,1,\"167,000000\",NaN,\"ns/op\",1e-8,500",
            "\"lab2.jmh.SystemFunctionLatencyBenchmark.sampleSystem:sampleSystem·p0.99\",\"sample\",1,1,\"209,000000\",NaN,\"ns/op\",1e-8,500"));

    Path out = tempDir.resolve("plots");
    LatencyReportGenerator.generate(input, out);

    List<String> summary = Files.readAllLines(out.resolve("latency-summary.csv"));
    assertAll(
        () -> assertEquals("epsilon;maxTerms;medianNs;p90Ns;p99Ns", summary.get(0)),
        () -> assertEquals("1e-4;500;42.000;84.000;125.000", summary.get(1)),
        () -> assertEquals("1e-8;500;166.000;167.000;209.000", summary.get(2)),
        () -> assertTrue(Files.exists(out.resolve("latency-median.svg"))),
        () -> assertTrue(Files.exists(out.resolve("latency-p90.svg"))),
        () -> assertTrue(Files.exists(out.resolve("latency-p99.svg"))),
        () -> assertTrue(Files.readString(out.resolve("latency-p99.svg")).contains("P99 latency by precision")));
  }

  @Test
  void read_rows_ignores_incomplete_percentile_groups() throws Exception {
    Path input = tempDir.resolve("latency.csv");
    Files.write(
        input,
        List.of(
            "\"Benchmark\",\"Mode\",\"Threads\",\"Samples\",\"Score\",\"Score Error (99,9%)\",\"Unit\",\"Param: epsilon\",\"Param: maxTerms\"",
            "\"lab2.jmh.SystemFunctionLatencyBenchmark.sampleSystem:sampleSystem·p0.50\",\"sample\",1,1,\"42,000000\",NaN,\"ns/op\",1e-4,500"));

    assertTrue(LatencyReportGenerator.readRows(input).isEmpty());
  }
}
