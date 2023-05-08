package it.pagopa.interop.probing.caller.util.logging.impl;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.stereotype.Component;
import it.pagopa.interop.probing.caller.dto.TelemetryDto;
import it.pagopa.interop.probing.caller.util.logging.Logger;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Component
public class LoggerImpl implements Logger {


  @Override
  public void logMessageReceiver(Long id) {
    log.info("Writing message, id={}", id);
  }


  @Override
  public void logMessageCallProbing(String technology, String basePath) {
    log.info("Calling {} service with base path: {}", technology, basePath);
  }

  @Override
  public void logResultCallProbing(int code, String detail) {
    log.info("Response {} with detail: {}", code, detail);
  }

  @Override
  public void logMessageResponseCallProbing(TelemetryDto telemetry) {
    log.info("Result: {}", telemetry.toString());
  }

  @Override
  public void logMessageSend(Long id, String topic) {
    log.info("Service with record id {} has been published in {} SQS", id, topic);
  }

  @Override
  public void logMessageException(Exception exception) {
    log.error(ExceptionUtils.getStackTrace(exception));
  }

}
