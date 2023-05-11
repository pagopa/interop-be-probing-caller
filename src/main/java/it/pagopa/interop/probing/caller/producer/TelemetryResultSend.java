package it.pagopa.interop.probing.caller.producer;

import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.pagopa.interop.probing.caller.dto.TelemetryDto;
import it.pagopa.interop.probing.caller.util.constant.ProjectConstants;
import it.pagopa.interop.probing.caller.util.logging.Logger;


@Service
public class TelemetryResultSend {

  @Value("${amazon.sqs.end-point.telemetry-result-queue}")
  private String sqsUrl;

  @Autowired
  private AmazonSQS amazonSQS;

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private Logger logger;


  public void sendMessage(TelemetryDto service) throws IOException {
    SendMessageRequest sendMessageRequest = new SendMessageRequest().withQueueUrl(sqsUrl)
        .withMessageBody(objectMapper.writeValueAsString(service));
    amazonSQS.sendMessage(sendMessageRequest);
    logger.logMessagePushedToQueue(service.eserviceRecordId(),
        ProjectConstants.SQS_TELEMETRY_RESULT);
  }
}
