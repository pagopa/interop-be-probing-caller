package it.pagopa.interop.probing.caller.util.logging;

import it.pagopa.interop.probing.caller.dto.impl.TelemetryDto;

public interface Logger {

  void logMessageReceiver(Long id, String threadId);

  void logMessagePushedToQueue(long eserviceRecordId, String queueLabel);

  void logMessageException(Exception exception);

  void logMessageHandledException(Exception exception);

  void logMessageCallProbing(String technology, String url);

  void logResultCallProbing(int code, String body, long elapsedTime);

  void logMessageResponseCallProbing(TelemetryDto telemetry);

}
