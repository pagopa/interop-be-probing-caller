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
  public void logMessageCallProbing(String technology, String url) {
    log.info("Calling public authority service, technology={}, url={}", technology, url);
  }

  @Override
  public void logResultCallProbing(int code, String body) {
    log.info("Probing response received, code={}, body={}", code, body);
  }

  @Override
  public void logMessageResponseCallProbing(TelemetryDto telemetry) {
    log.info("Result: {}", telemetry.toString());
  }

  public void logMessagePushedToQueue(long eserviceRecordId, String queueUrl, String queueGroupId) {
    log.info("message has been pushed in queue. eserviceRecordId={}, queue={}, groupId={}",
        eserviceRecordId, queueUrl, queueGroupId);
  }

  @Override
  public void logMessageException(Exception exception) {
    log.error(ExceptionUtils.getStackTrace(exception));
  }

}
