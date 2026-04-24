package lab2;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import lab2.api.CotModule;
import lab2.api.Log10Module;
import lab2.api.LnModule;
import lab2.api.SecModule;
import lab2.api.SystemFunctionModule;
import lab2.impl.RealCosModule;
import lab2.impl.RealCotModule;
import lab2.impl.RealCscModule;
import lab2.impl.RealLnModule;
import lab2.impl.RealLog10Module;
import lab2.impl.RealSecModule;
import lab2.impl.RealSinModule;
import lab2.impl.RealSystemFunction;
import lab2.impl.RealTgModule;
import org.junit.jupiter.api.Test;

class EquivalencePartitionTest {

  @Test
  void ln_domain_positive_valid() {
    ComputationContext ctx = new ComputationContext(1e-11, 50000);
    LnModule ln = new RealLnModule(ctx);
    assertEquals(Math.log(3.3), ln.ln(3.3), 1e-7);
  }

  @Test
  void ln_domain_non_positive_invalid() {
    ComputationContext ctx = new ComputationContext(1e-11, 50000);
    LnModule ln = new RealLnModule(ctx);
    assertThrows(IllegalArgumentException.class, () -> ln.ln(0.0));
    assertThrows(IllegalArgumentException.class, () -> ln.ln(-2.0));
  }

  @Test
  void log10_domain_follows_ln() {
    ComputationContext ctx = new ComputationContext(1e-11, 50000);
    Log10Module log10 = new RealLog10Module(new RealLnModule(ctx));
    assertThrows(IllegalArgumentException.class, () -> log10.log10(-0.1));
  }

  @Test
  void system_domain_has_expected_singular_points() {
    ComputationContext ctx = new ComputationContext(1e-12, 50000);
    ModuleStacks.Stack stack = ModuleStacks.Stack.allReal(ctx);
    assertThrows(IllegalArgumentException.class, () -> stack.system().system(0.0));
    assertThrows(IllegalArgumentException.class, () -> stack.system().system(-Math.PI));
    assertThrows(IllegalArgumentException.class, () -> stack.system().system(-Math.PI / 2.0));
    assertThrows(IllegalArgumentException.class, () -> stack.system().system(1.0));
  }

  @Test
  void system_valid_interval_smooth() {
    ComputationContext ctx = new ComputationContext(1e-12, 50000);
    SystemFunctionModule sys = ModuleStacks.Stack.allReal(ctx).system();
    double v = sys.system(0.25);
    assertTrue(Double.isFinite(v));
  }

  @Test
  void boundary_small_positive_x() {
    ComputationContext ctx = new ComputationContext(1e-10, 50000);
    RealSinModule sin = new RealSinModule(ctx);
    RealCosModule cos = new RealCosModule(sin);
    CotModule cot = new RealCotModule(sin, cos);
    SecModule sec = new RealSecModule(cos);
    RealCscModule csc = new RealCscModule(sin);
    LnModule ln = new RealLnModule(ctx);
    Log10Module log10 = new RealLog10Module(ln);
    SystemFunctionModule sys = new RealSystemFunction(sin, cot, sec, csc, ln, log10);
    double x = 0.05;
    double l = Math.log(x);
    double expected = Math.pow(Math.pow((l / l) - l, 3) - (Math.log(x) / Math.log(10.0)), 2);
    assertEquals(expected, sys.system(x), 1e-5);
  }
}
