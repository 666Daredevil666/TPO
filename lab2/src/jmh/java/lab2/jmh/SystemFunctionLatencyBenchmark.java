package lab2.jmh;

import java.util.concurrent.TimeUnit;
import lab2.ComputationContext;
import lab2.ModuleStacks;
import lab2.api.SystemFunctionModule;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.infra.Blackhole;

@BenchmarkMode(Mode.SampleTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@Warmup(iterations = 2, time = 1)
@Measurement(iterations = 3, time = 1)
@Fork(1)
public class SystemFunctionLatencyBenchmark {

  @Benchmark
  public void sampleSystem(BenchState state, Blackhole bh) {
    bh.consume(state.system.system(state.x));
  }

  @State(Scope.Benchmark)
  public static class BenchState {

    @Param({"1e-4", "1e-8", "1e-12", "1e-14"})
    public String epsilon;

    @Param({"500", "5000", "20000"})
    public int maxTerms;

    public SystemFunctionModule system;
    public double x;

    @Setup
    public void setup() {
      ComputationContext ctx =
          new ComputationContext(Double.parseDouble(epsilon), maxTerms);
      system = ModuleStacks.Stack.allReal(ctx).system();
      x = 0.37;
    }
  }
}
