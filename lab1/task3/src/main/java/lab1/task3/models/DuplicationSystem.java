package lab1.task3.models;

import java.util.Objects;

public class DuplicationSystem {
  private final Person owner;
  private final PortableComputer portableComputer;
  private final MainComputer mainComputer;
  private final Ship ship;
  private final CommunicationChannel channel;
  private FailureType failureType = FailureType.NONE;

  public DuplicationSystem(
    Person owner,
    PortableComputer portableComputer,
    MainComputer mainComputer,
    Ship ship,
    CommunicationChannel channel
  ) {
    this.owner = Objects.requireNonNull(owner, "owner must not be null");
    this.portableComputer = Objects.requireNonNull(portableComputer, "portableComputer must not be null");
    this.mainComputer = Objects.requireNonNull(mainComputer, "mainComputer must not be null");
    this.ship = Objects.requireNonNull(ship, "ship must not be null");
    this.channel = Objects.requireNonNull(channel, "channel must not be null");
  }

  public Person getOwner() {
    return owner;
  }

  public FailureType getFailureType() {
    return failureType;
  }

  public void setFailureType(FailureType failureType) {
    this.failureType = Objects.requireNonNull(failureType, "failureType must not be null");
  }

  public boolean isValid() {
    return ship.getMainComputer() == mainComputer
      && portableComputer.isConnected()
      && portableComputer.getMainComputer() == mainComputer
      && portableComputer.getChannel() == channel
      && channel.isSubEther()
      && portableComputer.isFullyDuplicated();
  }

  public boolean isOperational() {
    return isValid() && failureType != FailureType.GLOBAL_HARDWARE_FAILURE;
  }
}
