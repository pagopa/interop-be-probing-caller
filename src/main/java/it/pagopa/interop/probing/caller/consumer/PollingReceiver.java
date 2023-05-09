package it.pagopa.interop.probing.caller.consumer;

import java.io.IOException;
import java.time.OffsetDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.awspring.cloud.messaging.listener.SqsMessageDeletionPolicy;
import io.awspring.cloud.messaging.listener.annotation.SqsListener;
import it.pagopa.interop.probing.caller.dto.EserviceContentDto;
import it.pagopa.interop.probing.caller.dto.PollingDto;
import it.pagopa.interop.probing.caller.producer.PollingResultSend;
import it.pagopa.interop.probing.caller.producer.TelemetryResultSend;
import it.pagopa.interop.probing.caller.util.ClientUtil;
import it.pagopa.interop.probing.caller.util.logging.Logger;


@Component
public class PollingReceiver {

  @Autowired
  ObjectMapper mapper;

  @Autowired
  PollingResultSend pollingResultSend;

  @Autowired
  TelemetryResultSend telemetryResultSend;

  @Autowired
  private Logger logger;

  @Autowired
  private ClientUtil clientUtil;


  @SqsListener(value = "${amazon.sqs.end-point.poll-queue}",
      deletionPolicy = SqsMessageDeletionPolicy.ON_SUCCESS)
  public void receiveStringMessage(final String message) throws IOException {

    EserviceContentDto service = mapper.readValue(message, EserviceContentDto.class);

    try {
      telemetryResultSend.sendMessage(clientUtil.callProbing(service));

      PollingDto polling = PollingDto.builder().eserviceRecordId(service.eserviceRecordId())
          .responseTime(OffsetDateTime.now()).build();

      pollingResultSend.sendMessage(polling);
    } catch (IOException e) {
      logger.logMessageException(e);
      throw e;
    }
  }
}
