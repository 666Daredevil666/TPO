package lab1.task3.models;

import java.util.Objects;

public class Ship {
  private final String name;
  private MainComputer mainComputer;

  public Ship(String name) {
    this.name = Objects.requireNonNull(name, "name must not be null");
  }

  public String getName() {
    return name;
  }

  public MainComputer getMainComputer() {
    return mainComputer;
  }

  public void setMainComputer(MainComputer mainComputer) {
    this.mainComputer = Objects.requireNonNull(mainComputer, "mainComputer must not be null");
  }
}
