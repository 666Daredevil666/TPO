package lab1.task3.models;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class DuplicationSystemTest {

  private DuplicationSystem createValidSystem() {
    Person ford = new Person("Форд");
    Ship ship = new Ship("Heart of Gold");
    MainComputer mainComputer = new MainComputer("MC-1");
    ship.setMainComputer(mainComputer);

    PortableComputer portableComputer = new PortableComputer("PC-1");
    CommunicationChannel channel = new CommunicationChannel("sub-ether");
    portableComputer.connectTo(mainComputer, channel);
    portableComputer.setFullyDuplicated(true);

    return new DuplicationSystem(ford, portableComputer, mainComputer, ship, channel);
  }

  @Test
  @DisplayName("Валидная система дублирования")
  void testValidSystem() {
    DuplicationSystem system = createValidSystem();

    assertTrue(system.isValid());
    assertTrue(system.isOperational());
  }

  @Test
  @DisplayName("Система остаётся работоспособной при локальном отказе")
  void testLocalFailure() {
    DuplicationSystem system = createValidSystem();

    system.setFailureType(FailureType.LOCAL_FAILURE);

    assertTrue(system.isValid());
    assertTrue(system.isOperational());
  }

  @Test
  @DisplayName("Глобальный аппаратный сбой делает систему неработоспособной")
  void testGlobalHardwareFailure() {
    DuplicationSystem system = createValidSystem();

    system.setFailureType(FailureType.GLOBAL_HARDWARE_FAILURE);

    assertTrue(system.isValid());
    assertFalse(system.isOperational());
  }

  @Test
  @DisplayName("Неверный тип канала связи делает систему невалидной")
  void testWrongChannelType() {
    Person ford = new Person("Форд");
    Ship ship = new Ship("Heart of Gold");
    MainComputer mainComputer = new MainComputer("MC-1");
    ship.setMainComputer(mainComputer);

    PortableComputer portableComputer = new PortableComputer("PC-1");
    CommunicationChannel channel = new CommunicationChannel("radio");
    portableComputer.connectTo(mainComputer, channel);
    portableComputer.setFullyDuplicated(true);

    DuplicationSystem system = new DuplicationSystem(ford, portableComputer, mainComputer, ship, channel);

    assertFalse(system.isValid());
    assertFalse(system.isOperational());
  }

  @Test
  @DisplayName("Отсутствие полного дублирования делает систему невалидной")
  void testMissingFullDuplication() {
    Person ford = new Person("Форд");
    Ship ship = new Ship("Heart of Gold");
    MainComputer mainComputer = new MainComputer("MC-1");
    ship.setMainComputer(mainComputer);

    PortableComputer portableComputer = new PortableComputer("PC-1");
    CommunicationChannel channel = new CommunicationChannel("sub-ether");
    portableComputer.connectTo(mainComputer, channel);

    DuplicationSystem system = new DuplicationSystem(ford, portableComputer, mainComputer, ship, channel);

    assertFalse(system.isValid());
    assertFalse(system.isOperational());
  }
}
