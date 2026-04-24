package lab2;

import lab2.api.CosModule;
import lab2.api.CotModule;
import lab2.api.CscModule;
import lab2.api.LnModule;
import lab2.api.Log10Module;
import lab2.api.SinModule;
import lab2.api.SecModule;
import lab2.api.SystemFunctionModule;
import lab2.api.TgModule;
import lab2.impl.RealCosModule;
import lab2.impl.RealCotModule;
import lab2.impl.RealCscModule;
import lab2.impl.RealLnModule;
import lab2.impl.RealLog10Module;
import lab2.impl.RealSecModule;
import lab2.impl.RealSinModule;
import lab2.impl.RealSystemFunction;
import lab2.impl.RealTgModule;

public final class ModuleStacks {

  public record Stack(
      SinModule sin,
      CosModule cos,
      TgModule tg,
      CotModule cot,
      SecModule sec,
      CscModule csc,
      LnModule ln,
      Log10Module log10,
      SystemFunctionModule system) {

    public static Stack allReal(ComputationContext ctx) {
      SinModule sin = new RealSinModule(ctx);
      CosModule cos = new RealCosModule(sin);
      TgModule tg = new RealTgModule(sin, cos);
      LnModule ln = new RealLnModule(ctx);
      CotModule cot = new RealCotModule(sin, cos);
      SecModule sec = new RealSecModule(cos);
      CscModule csc = new RealCscModule(sin);
      Log10Module log10 = new RealLog10Module(ln);
      SystemFunctionModule system = new RealSystemFunction(sin, cot, sec, csc, ln, log10);
      return new Stack(sin, cos, tg, cot, sec, csc, ln, log10, system);
    }

    public static Stack fromParts(
        SinModule sin,
        CosModule cos,
        TgModule tg,
        CotModule cot,
        SecModule sec,
        CscModule csc,
        LnModule ln,
        Log10Module log10) {
      SystemFunctionModule system = new RealSystemFunction(sin, cot, sec, csc, ln, log10);
      return new Stack(sin, cos, tg, cot, sec, csc, ln, log10, system);
    }
  }

  private ModuleStacks() {
  }
}
