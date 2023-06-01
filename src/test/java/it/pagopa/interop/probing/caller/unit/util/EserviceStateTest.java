package it.pagopa.interop.probing.caller.unit.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import it.pagopa.interop.probing.caller.util.EserviceStatus;

class EserviceStateTest {
  @Test
  @DisplayName("getValue should return the correct value")
  void testGetValue_thenReturnsCorrectValue() {
    assertEquals("OK", EserviceStatus.OK.getValue());
  }

  @Test
  @DisplayName("fromValue with 'OK' should return EserviceStatus.OK")
  void testFromValue_givenValueACTIVE_thenReturnsEserviceStateACTIVE() {
    EserviceStatus status = EserviceStatus.fromValue("OK");

    assertEquals(EserviceStatus.OK, status);
  }

  @Test
  @DisplayName("fromValue with 'KO' should return EserviceStatus.KO")
  void testFromValue_givenValueINACTIVE_thenReturnsEserviceStatusINACTIVE() {
    EserviceStatus status = EserviceStatus.fromValue("KO");

    assertEquals(EserviceStatus.KO, status);
  }

  @Test
  @DisplayName("fromValue with invalid value should throw an exception")
  void testFromValue_givenInvalidValue_thenThrowException() {
    assertThrows(IllegalArgumentException.class, () -> EserviceStatus.fromValue("SUCCESS"));
  }

  @Test
  @DisplayName("toString should return the string representation of EserviceStatus.KO")
  void testToString_givenValidValueKO_thenReturnsStringValue() {
    assertEquals("KO", EserviceStatus.KO.toString());
  }
}
