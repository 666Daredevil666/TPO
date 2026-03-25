package lab1.task3;

import lab1.task3.models.CommunicationChannel;
import lab1.task3.models.DuplicationSystem;
import lab1.task3.models.FailureType;
import lab1.task3.models.MainComputer;
import lab1.task3.models.Person;
import lab1.task3.models.PortableComputer;
import lab1.task3.models.Ship;

public class App {
  public static void main(String[] args) {
    Person ford = new Person("Форд");
    Ship ship = new Ship("Heart of Gold");
    MainComputer mainComputer = new MainComputer("MC-1");
    ship.setMainComputer(mainComputer);

    PortableComputer portableComputer = new PortableComputer("PC-1");
    CommunicationChannel channel = new CommunicationChannel("sub-ether");
    portableComputer.connectTo(mainComputer, channel);
    portableComputer.setFullyDuplicated(true);

    DuplicationSystem system = new DuplicationSystem(ford, portableComputer, mainComputer, ship, channel);
    System.out.println("Система валидна: " + system.isValid());
    System.out.println("Система работоспособна: " + system.isOperational());

    system.setFailureType(FailureType.GLOBAL_HARDWARE_FAILURE);
    System.out.println("После глобального сбоя: " + system.isOperational());
  }
}
