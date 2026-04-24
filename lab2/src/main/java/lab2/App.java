package lab2;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.function.DoubleUnaryOperator;
import lab2.csv.CsvExporter;

public final class App {

  public static void main(String[] args) throws Exception {
    if (args.length < 7) {
      System.exit(2);
      return;
    }
    String moduleName = args[0];
    double x0 = Double.parseDouble(args[1]);
    double x1 = Double.parseDouble(args[2]);
    double step = Double.parseDouble(args[3]);
    double eps = Double.parseDouble(args[4]);
    int maxTerms = Integer.parseInt(args[5]);
    Path out = Paths.get(args[6]);
    ComputationContext ctx = new ComputationContext(eps, maxTerms);
    ModuleStacks.Stack stack = ModuleStacks.Stack.allReal(ctx);
    DoubleUnaryOperator fn = select(moduleName, stack);
    CsvExporter.export(out, x0, x1, step, fn, ';');
  }

  private static DoubleUnaryOperator select(String moduleName, ModuleStacks.Stack stack) {
    return switch (moduleName.toUpperCase()) {
      case "SIN" -> stack.sin()::sin;
      case "COS" -> stack.cos()::cos;
      case "TG" -> stack.tg()::tg;
      case "COT" -> stack.cot()::cot;
      case "SEC" -> stack.sec()::sec;
      case "CSC" -> stack.csc()::csc;
      case "LN" -> stack.ln()::ln;
      case "LOG10" -> stack.log10()::log10;
      case "SYSTEM" -> stack.system()::system;
      default -> throw new IllegalArgumentException(moduleName);
    };
  }
}
