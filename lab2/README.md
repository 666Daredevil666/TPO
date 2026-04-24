# Лабораторная работа 2

## Вариант `320202`

Интеграционное тестирование программы, вычисляющей систему функций:

```text
x <= 0 : ((((sin(x) * cot(x)) ^ 2) / (sec(x) / csc(x))) + cot(x))
x > 0  : (((((ln(x) / ln(x)) - ln(x)) ^ 3) - log_10(x)) ^ 2)
```

Базовые функции `sin(x)` и `ln(x)` реализованы через разложение в ряд с задаваемой погрешностью. Остальные тригонометрические и логарифмические функции выражены через базовые модули.

## Структура

- `src/main/java/lab2/api` - интерфейсы модулей.
- `src/main/java/lab2/series` - базовые ряды `SinSeries` и `LnSeries`.
- `src/main/java/lab2/impl` - реальные реализации модулей и системы функций.
- `src/main/java/lab2/stub` - табличные заглушки для интеграционного тестирования.
- `src/main/java/lab2/csv` - CSV-выгрузка значений модулей.
- `src/main/java/lab2/benchmark` - генератор summary/графиков по JMH latency CSV.
- `src/jmh/java/lab2/jmh` - JMH latency benchmark.
- `docs` - отчёт и графики latency.

## Запуск

```bash
./gradlew test
./gradlew jacocoTestReport
./gradlew jmh
./gradlew generateLatencyPlots
```

CSV-выгрузка любого модуля:

```bash
./gradlew run --args="SYSTEM 0.1 2.0 0.1 1e-12 50000 output.csv"
```

Доступные имена модулей: `SIN`, `COS`, `TG`, `COT`, `SEC`, `CSC`, `LN`, `LOG10`, `SYSTEM`.

## Дополнительное задание

Для latency benchmarking используется JMH в режиме `SampleTime`. Бенчмарк вызывает `SystemFunctionModule.system(x)` при разных значениях `epsilon` и `maxTerms`, а затем формирует:

- `latency-summary.csv`;
- `latency-median.svg`;
- `latency-p90.svg`;
- `latency-p99.svg`.
