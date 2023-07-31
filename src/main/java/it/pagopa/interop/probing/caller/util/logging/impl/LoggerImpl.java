package it.pagopa.interop.probing.caller.util.logging.impl;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.stereotype.Component;
import it.pagopa.interop.probing.caller.dto.impl.TelemetryDto;
import it.pagopa.interop.probing.caller.util.logging.Logger;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Component
public class LoggerImpl implements Logger {

  @Override
  public void logMessageReceiver(Long id, String threadId) {
    log.info("Writing message, id={}. Message processed by thread {}", id, threadId);
  }

  @Override
  public void logMessageCallProbing(String technology, String url) {
    log.info("Calling public authority service, technology={}, url={}", technology, url);
  }

  @Override
  public void logResultCallProbing(int code, String body, long elapsedTime) {
    log.info("Probing response received, code={}, body={}, elapsedTime in millis={}", code, body,
        elapsedTime);
  }

  @Override
  public void logMessageResponseCallProbing(TelemetryDto telemetry) {
    log.info("Result: {}", telemetry.toString());
  }

  public void logMessagePushedToQueue(long eserviceRecordId, String queueLabel) {
    log.info("Service with record id {} has been published in SQS {} queue.", eserviceRecordId,
        queueLabel);
  }

  @Override
  public void logMessageException(Exception exception) {
    log.error(ExceptionUtils.getStackTrace(exception));
  }

  @Override
  public void logMessageHandledException(Exception exception) {
    log.info(ExceptionUtils.getStackTrace(exception));
  }

}
