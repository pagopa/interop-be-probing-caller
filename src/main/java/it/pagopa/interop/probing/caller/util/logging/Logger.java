package it.pagopa.interop.probing.caller.util.logging;

public interface Logger {


  void logMessageReceiver(String id);

  void logMessagePollingSend(String id);

  void logMessageTelemetrySend(String name);

  void logMessageException(Exception exception);
}
