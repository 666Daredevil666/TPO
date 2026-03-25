package lab1.task3.models;

import java.util.Objects;

public class PortableComputer {
  private final String id;
  private MainComputer mainComputer;
  private CommunicationChannel channel;
  private boolean fullyDuplicated;

  public PortableComputer(String id) {
    this.id = Objects.requireNonNull(id, "id must not be null");
  }

  public String getId() {
    return id;
  }

  public void connectTo(MainComputer mainComputer, CommunicationChannel channel) {
    this.mainComputer = Objects.requireNonNull(mainComputer, "mainComputer must not be null");
    this.channel = Objects.requireNonNull(channel, "channel must not be null");
  }

  public MainComputer getMainComputer() {
    return mainComputer;
  }

  public CommunicationChannel getChannel() {
    return channel;
  }

  public boolean isConnected() {
    return mainComputer != null && channel != null;
  }

  public boolean isFullyDuplicated() {
    return fullyDuplicated;
  }

  public void setFullyDuplicated(boolean fullyDuplicated) {
    this.fullyDuplicated = fullyDuplicated;
  }
}
