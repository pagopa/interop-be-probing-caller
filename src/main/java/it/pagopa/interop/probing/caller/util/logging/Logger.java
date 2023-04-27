package it.pagopa.interop.probing.caller.util.logging;

public interface Logger {


  void logMessageReceiver(String id);

  void logMessagePollingSend(String id);

  void logMessageTelemetrySend(Long id);

  void logMessageException(Exception exception);
}
