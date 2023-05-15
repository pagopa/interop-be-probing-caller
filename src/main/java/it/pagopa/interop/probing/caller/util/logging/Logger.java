package it.pagopa.interop.probing.caller.util.logging;

import it.pagopa.interop.probing.caller.dto.impl.TelemetryDto;

public interface Logger {

  void logMessageReceiver(Long id);

  void logMessagePushedToQueue(long eserviceRecordId, String queueLabel);

  void logMessageException(Exception exception);

  void logMessageCallProbing(String technology, String url);

  void logResultCallProbing(int code, String body);

  void logMessageResponseCallProbing(TelemetryDto telemetry);

}
