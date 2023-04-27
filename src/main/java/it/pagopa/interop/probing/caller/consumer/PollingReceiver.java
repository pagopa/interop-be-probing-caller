package it.pagopa.interop.probing.caller.consumer;

import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.awspring.cloud.messaging.listener.SqsMessageDeletionPolicy;
import io.awspring.cloud.messaging.listener.annotation.SqsListener;
import it.pagopa.interop.probing.caller.dto.EserviceContentDto;
import it.pagopa.interop.probing.caller.util.logging.Logger;


@Component
public class PollingReceiver {

  @Autowired
  ObjectMapper mapper;

  @Autowired
  private Logger logger;

  @SqsListener(value = "${amazon.sqs.end-point.poll-queue}",
      deletionPolicy = SqsMessageDeletionPolicy.ON_SUCCESS)
  public void receiveStringMessage(final String message) throws IOException {

    EserviceContentDto service = mapper.readValue(message, EserviceContentDto.class);

    logger.logMessageReceiver(service.getEserviceRecordId());
  }
}