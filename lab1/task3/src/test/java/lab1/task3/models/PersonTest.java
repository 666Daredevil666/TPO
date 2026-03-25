package lab1.task3.models;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PersonTest {

  @Test
  @DisplayName("Корректное создание персонажа")
  void testPersonCreation() {
    Person ford = new Person("Форд");
    assertEquals("Форд", ford.getName());
  }
}
