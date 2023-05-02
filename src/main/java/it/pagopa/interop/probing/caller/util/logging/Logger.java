package it.pagopa.interop.probing.caller.util.logging;

public interface Logger {

  void logMessageReceiver(Long id);

  void logMessagePollingSend(Long id);

  void logMessageTelemetrySend(Long id);

  void logMessageException(Exception exception);

}
