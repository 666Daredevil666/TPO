package lab1.task3.models;

import java.util.Objects;

public class CommunicationChannel {
  private final String type;

  public CommunicationChannel(String type) {
    this.type = Objects.requireNonNull(type, "type must not be null");
  }

  public String getType() {
    return type;
  }

  public boolean isSubEther() {
    return "sub-ether".equals(type);
  }
}
