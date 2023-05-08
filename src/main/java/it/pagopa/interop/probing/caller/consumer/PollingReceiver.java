package it.pagopa.interop.probing.caller.consumer;

import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.pagopa.interop.probing.caller.dto.EserviceContentDto;
import it.pagopa.interop.probing.caller.producer.PollingResultSend;
import it.pagopa.interop.probing.caller.producer.TelemetryResultSend;
import it.pagopa.interop.probing.caller.util.ClientUtil;
import it.pagopa.interop.probing.caller.util.EserviceTechnology;
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


  // @SqsListener(value = "${amazon.sqs.end-point.poll-queue}",
  // deletionPolicy = SqsMessageDeletionPolicy.ON_SUCCESS)
  public void receiveStringMessage(final String message) throws IOException {
    // EserviceContentDto service = mapper.readValue(message, EserviceContentDto.class);
    // EserviceContentDto service =
    // EserviceContentDto.builder().basePath(new String[] {"http://IT-PF23YL12:8088/Probing"})
    // .eserviceRecordId(1L).technology(EserviceTechnology.SOAP).build();
    EserviceContentDto service =
        EserviceContentDto.builder().basePath(new String[] {"http://localhost:3000/probing"})
            .eserviceRecordId(1L).technology(EserviceTechnology.REST).build();
    try {
      telemetryResultSend.sendMessage(clientUtil.callProbing(service));
      pollingResultSend.sendMessage(service);
    } catch (IOException e) {
      logger.logMessageException(e);
      throw e;
    }
  }
}
