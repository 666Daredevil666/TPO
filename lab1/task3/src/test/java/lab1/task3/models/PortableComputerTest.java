package lab1.task3.models;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PortableComputerTest {

  @Test
  @DisplayName("Подключение миниатюрного ранцевого компьютера к главному компьютеру")
  void testConnectionToMainComputer() {
    PortableComputer portableComputer = new PortableComputer("PC-1");
    MainComputer mainComputer = new MainComputer("MC-1");
    CommunicationChannel channel = new CommunicationChannel("sub-ether");

    portableComputer.connectTo(mainComputer, channel);

    assertTrue(portableComputer.isConnected());
    assertEquals(mainComputer, portableComputer.getMainComputer());
    assertEquals(channel, portableComputer.getChannel());
  }

  @Test
  @DisplayName("Признак полного дублирования")
  void testFullDuplication() {
    PortableComputer portableComputer = new PortableComputer("PC-1");
    assertFalse(portableComputer.isFullyDuplicated());

    portableComputer.setFullyDuplicated(true);
    assertTrue(portableComputer.isFullyDuplicated());
  }
}
