package it.pagopa.interop.probing.caller.unit.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import it.pagopa.interop.probing.caller.util.EserviceTechnology;

class EserviceTechnologyTest {

  @Test
  @DisplayName("getValue should return the correct value")
  void testGetValue_thenReturnsCorrectValue() {
    assertEquals("REST", EserviceTechnology.REST.getValue());
  }

  @Test
  @DisplayName("fromValue with 'REST' should return EserviceTechnology.REST")
  void testFromValue_givenValueREST_thenReturnsEserviceTechnologyREST() {
    EserviceTechnology technology = EserviceTechnology.fromValue("REST");

    assertEquals(EserviceTechnology.REST, technology);
  }

  @Test
  @DisplayName("fromValue with 'SOAP' should return EserviceTechnology.SOAP")
  void testFromValue_givenValueSOAP_thenReturnsEserviceTechnologySOAP() {
    EserviceTechnology technology = EserviceTechnology.fromValue("SOAP");

    assertEquals(EserviceTechnology.SOAP, technology);
  }

  @Test
  @DisplayName("fromValue with invalid value should throw an exception")
  void testFromValue_givenInvalidValue_thenThrowException() {
    assertThrows(IllegalArgumentException.class, () -> EserviceTechnology.fromValue("rest"));
  }

  @Test
  @DisplayName("toString should return the string representation of EserviceTechnology.SOAP")
  void testToString_givenValidValueSOAP_thenReturnsStringValue() {
    assertEquals("SOAP", EserviceTechnology.SOAP.toString());
  }
}
