package lab2;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import lab2.csv.CsvExporter;
import lab2.stub.StubCosModule;
import lab2.stub.StubCotModule;
import lab2.stub.StubCscModule;
import lab2.stub.StubLnModule;
import lab2.stub.StubLog10Module;
import lab2.stub.StubSecModule;
import lab2.stub.StubSinModule;
import lab2.stub.StubSystemFunction;
import lab2.stub.StubTgModule;
import lab2.stub.Table1D;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class StubAndCsvTest {

  @TempDir
  Path tempDir;

  @Test
  void table_sorts_points_and_interpolates() {
    Table1D table = new Table1D(new double[] {2.0, 0.0, 1.0}, new double[] {20.0, 0.0, 10.0});

    assertAll(
        () -> assertEquals(5.0, table.valueAt(0.5), 1e-12),
        () -> assertEquals(15.0, table.valueAt(1.5), 1e-12),
        () -> assertEquals(-10.0, table.valueAt(-1.0), 1e-12),
        () -> assertEquals(30.0, table.valueAt(3.0), 1e-12));
  }

  @Test
  void table_rejects_invalid_shapes() {
    assertAll(
        () -> assertThrows(IllegalArgumentException.class, () -> new Table1D(new double[] {}, new double[] {})),
        () -> assertThrows(
            IllegalArgumentException.class,
            () -> new Table1D(new double[] {1.0}, new double[] {1.0, 2.0})));
  }

  @Test
  void table_with_duplicate_x_uses_first_duplicate_value() {
    Table1D table = new Table1D(new double[] {1.0, 1.0, 2.0}, new double[] {7.0, 9.0, 20.0});

    assertEquals(7.0, table.valueAt(1.0), 1e-12);
  }

  @Test
  void all_table_stubs_delegate_to_table() {
    Table1D table = new Table1D(new double[] {0.0, 1.0}, new double[] {2.0, 4.0});

    assertAll(
        () -> assertEquals(3.0, new StubSinModule(table).sin(0.5), 1e-12),
        () -> assertEquals(3.0, new StubCosModule(table).cos(0.5), 1e-12),
        () -> assertEquals(3.0, new StubTgModule(table).tg(0.5), 1e-12),
        () -> assertEquals(3.0, new StubCotModule(table).cot(0.5), 1e-12),
        () -> assertEquals(3.0, new StubSecModule(table).sec(0.5), 1e-12),
        () -> assertEquals(3.0, new StubCscModule(table).csc(0.5), 1e-12),
        () -> assertEquals(3.0, new StubLnModule(table).ln(0.5), 1e-12),
        () -> assertEquals(3.0, new StubLog10Module(table).log10(0.5), 1e-12),
        () -> assertEquals(3.0, new StubSystemFunction(table).system(0.5), 1e-12));
  }

  @Test
  void csv_exporter_writes_header_and_nan_for_singular_points() throws Exception {
    Path out = tempDir.resolve("module.csv");

    CsvExporter.export(
        out,
        0.0,
        0.2,
        0.1,
        x -> {
          if (Math.abs(x - 0.1) < 1e-12) {
            throw new IllegalArgumentException("singular point");
          }
          return x * 2.0;
        },
        ';');

    List<String> lines = Files.readAllLines(out);
    assertAll(
        () -> assertEquals("X;Y", lines.get(0)),
        () -> assertEquals("0.0;0.0", lines.get(1)),
        () -> assertEquals("0.1;NaN", lines.get(2)),
        () -> assertEquals("0.2;0.4", lines.get(3)));
  }

  @Test
  void csv_exporter_rejects_invalid_bounds_and_step() {
    Path out = tempDir.resolve("module.csv");

    assertAll(
        () -> assertThrows(IllegalArgumentException.class, () -> CsvExporter.export(out, 0.0, 1.0, 0.0, x -> x, ';')),
        () -> assertThrows(IllegalArgumentException.class, () -> CsvExporter.export(out, 1.0, 0.0, 0.1, x -> x, ';')));
  }

  @Test
  void app_can_export_selected_module_to_csv() throws Exception {
    Path out = tempDir.resolve("sin.csv");

    App.main(new String[] {"SIN", "0.0", "0.2", "0.1", "1e-12", "20000", out.toString()});

    List<String> lines = Files.readAllLines(out);
    assertAll(
        () -> assertFalse(lines.isEmpty()),
        () -> assertEquals("X;Y", lines.get(0)),
        () -> assertTrue(lines.get(1).startsWith("0.0;0.0")));
  }

  @Test
  void computation_context_rejects_invalid_precision_settings() {
    assertAll(
        () -> assertThrows(IllegalArgumentException.class, () -> new ComputationContext(0.0, 10)),
        () -> assertThrows(IllegalArgumentException.class, () -> new ComputationContext(Double.NaN, 10)),
        () -> assertThrows(IllegalArgumentException.class, () -> new ComputationContext(1e-9, 0)));
  }
}
