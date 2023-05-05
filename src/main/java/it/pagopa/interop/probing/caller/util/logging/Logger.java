package it.pagopa.interop.probing.caller.util.logging;

import it.pagopa.interop.probing.caller.dto.TelemetryDto;

public interface Logger {

  void logMessageReceiver(Long id);

  void logMessageSend(Long id, String topic);

  void logMessageException(Exception exception);

  void logMessageCallProbing(String technology, String basePath);

  void logMessageResponseCallProbing(TelemetryDto telemetry);

}
