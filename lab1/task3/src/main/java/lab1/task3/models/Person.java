package lab1.task3.models;

import java.util.Objects;

public class Person {
  private final String name;

  public Person(String name) {
    this.name = Objects.requireNonNull(name, "name must not be null");
  }

  public String getName() {
    return name;
  }
}
