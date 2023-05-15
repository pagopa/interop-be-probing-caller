package it.pagopa.interop.probing.caller.consumer;

import java.io.IOException;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.awspring.cloud.messaging.listener.SqsMessageDeletionPolicy;
import io.awspring.cloud.messaging.listener.annotation.SqsListener;
import it.pagopa.interop.probing.caller.dto.impl.EserviceContentDto;
import it.pagopa.interop.probing.caller.dto.impl.PollingDto;
import it.pagopa.interop.probing.caller.dto.impl.TelemetryDto;
import it.pagopa.interop.probing.caller.producer.PollingResultSend;
import it.pagopa.interop.probing.caller.producer.TelemetryResultSend;
import it.pagopa.interop.probing.caller.util.ClientUtil;
import it.pagopa.interop.probing.caller.util.logging.Logger;


@Component
public class PollingReceiver {

  @Autowired
  ObjectMapper mapper;

  @Autowired
  private PollingResultSend pollingResultSend;

  @Autowired
  private TelemetryResultSend telemetryResultSend;

  @Autowired
  private Logger logger;

  @Autowired
  private ClientUtil clientUtil;

  @SqsListener(value = "${amazon.sqs.end-point.poll-queue}",
      deletionPolicy = SqsMessageDeletionPolicy.ON_SUCCESS)
  public void receiveStringMessage(final String message) throws IOException {

    EserviceContentDto service = mapper.readValue(message, EserviceContentDto.class);

    try {
      TelemetryDto telemetryDto = clientUtil.callProbing(service);
      telemetryResultSend.sendMessage(telemetryDto);
      pollingResultSend
          .sendMessage(PollingDto.builder().eserviceRecordId(service.eserviceRecordId())
              .responseReceived(OffsetDateTime.now(ZoneOffset.UTC)).status(telemetryDto.status())
              .build());
    } catch (IOException e) {
      logger.logMessageException(e);
      throw e;
    }
  }
}
