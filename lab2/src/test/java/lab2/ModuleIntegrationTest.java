package lab2;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import lab2.api.Log10Module;
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
import lab2.stub.StubLog10Module;
import lab2.stub.Table1D;
import lab2.variant.Variant320202;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Интеграционные тесты модулей системы функций")
class ModuleIntegrationTest {

  @Test
  @DisplayName("Полностью реальная сборка системы совпадает с Math-эталоном")
  void full_real_stack_matches_reference() {
    // Собираем все реальные модули через ModuleStacks и проверяем итоговую
    // логарифмическую ветку системы в обычной допустимой точке x > 0.
    ComputationContext ctx = new ComputationContext(1e-12, 50000);
    ModuleStacks.Stack stack = ModuleStacks.Stack.allReal(ctx);
    double x = 0.41;
    double ln = Math.log(x);
    double expected = Math.pow(Math.pow((ln / ln) - ln, 3) - (Math.log(x) / Math.log(10.0)), 2);
    assertEquals(expected, stack.system().system(x), 5e-7);
  }

  @Test
  @DisplayName("cos(x) интегрирован через базовый модуль sin(x)")
  void cos_depends_on_sin_module() {
    // По заданию небазовые тригонометрические функции строятся через базовые:
    // здесь проверяем, что cos корректно получается через sin(x + pi/2).
    ComputationContext ctx = new ComputationContext(1e-12, 20000);
    RealSinModule sin = new RealSinModule(ctx);
    RealCosModule cos = new RealCosModule(sin);
    double x = 0.55;
    assertEquals(Math.cos(x), cos.cos(x), 1e-9);
  }

  @Test
  @DisplayName("tg(x) бросает исключение в точке, где cos(x) равен нулю")
  void tg_throws_at_cos_zero() {
    // Тангенс определяется как sin/cos, поэтому около pi/2 знаменатель
    // становится нулевым и модуль обязан сообщить о сингулярности.
    ComputationContext ctx = new ComputationContext(1e-12, 20000);
    RealSinModule sin = new RealSinModule(ctx);
    RealCosModule cos = new RealCosModule(sin);
    RealTgModule tg = new RealTgModule(sin, cos);
    assertThrows(IllegalArgumentException.class, () -> tg.tg(Math.PI / 2.0));
  }

  @Test
  @DisplayName("Арифметика двух ветвей варианта 320202 считается правильно")
  void variant_branches_have_expected_arithmetic() {
    // Изолированно проверяем формулы ветвей без погрешностей рядов и модулей.
    assertEquals(15.0, Variant320202.branchXLe0(2.0, 3.0, 6.0, 2.0), 0.0);
    assertEquals(4.0, Variant320202.branchXGt0(2.0, 1.0), 0.0);
  }

  @Test
  @DisplayName("Смешанная сборка: реальные тригонометрические модули и заглушка log10")
  void hybrid_real_trig_stub_log10_matches_reference() {
    // Этот тест демонстрирует пошаговую интеграцию: log10 временно заменён
    // табличной заглушкой, остальные зависимости уже реальные.
    ComputationContext ctx = new ComputationContext(1e-12, 50000);
    RealSinModule sin = new RealSinModule(ctx);
    RealCosModule cos = new RealCosModule(sin);
    RealCotModule cot = new RealCotModule(sin, cos);
    RealSecModule sec = new RealSecModule(cos);
    RealCscModule csc = new RealCscModule(sin);
    RealLnModule ln = new RealLnModule(ctx);
    double x = 0.44;
    double[] xs = {0.1, 0.44, 0.9};
    double[] ys = new double[3];
    for (int i = 0; i < xs.length; i++) {
      ys[i] = Math.log(xs[i]) / Math.log(10.0);
    }
    Log10Module log10 = new StubLog10Module(new Table1D(xs, ys));
    SystemFunctionModule sys = new RealSystemFunction(sin, cot, sec, csc, ln, log10);
    double l = Math.log(x);
    double expected = Math.pow(Math.pow((l / l) - l, 3) - (Math.log(x) / Math.log(10.0)), 2);
    assertEquals(expected, sys.system(x), 1e-8);
  }

  @Test
  @DisplayName("Табличная заглушка log10 согласована с реальной реализацией")
  void stub_log10_table_aligned_with_real_reference() {
    // Значения таблицы для StubLog10Module берутся из реального log10, поэтому
    // в узловой точке x = 0.5 смешанная система должна совпасть с эталоном.
    double[] xs = {0.2, 0.5, 0.8};
    ComputationContext ctx = new ComputationContext(1e-12, 50000);
    RealLnModule ln = new RealLnModule(ctx);
    RealLog10Module realLog10 = new RealLog10Module(ln);
    double[] log10y = new double[3];
    for (int i = 0; i < 3; i++) {
      log10y[i] = realLog10.log10(xs[i]);
    }
    ComputationContext ctxTrig = new ComputationContext(1e-12, 20000);
    RealSinModule sin = new RealSinModule(ctxTrig);
    RealCosModule cos = new RealCosModule(sin);
    RealCotModule cot = new RealCotModule(sin, cos);
    RealSecModule sec = new RealSecModule(cos);
    RealCscModule csc = new RealCscModule(sin);
    SystemFunctionModule sys =
        new RealSystemFunction(sin, cot, sec, csc, ln, new StubLog10Module(new Table1D(xs, log10y)));
    double x = 0.5;
    double l = Math.log(x);
    double expected = Math.pow(Math.pow((l / l) - l, 3) - realLog10.log10(x), 2);
    assertEquals(expected, sys.system(x), 1e-8);
  }
}
