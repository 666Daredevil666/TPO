package lab1.task3.models;

import java.util.Objects;

public class MainComputer {
  private final String id;

  public MainComputer(String id) {
    this.id = Objects.requireNonNull(id, "id must not be null");
  }

  public String getId() {
    return id;
  }
}
