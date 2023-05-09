package it.pagopa.interop.probing.caller.producer;

import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.pagopa.interop.probing.caller.dto.PollingDto;
import it.pagopa.interop.probing.caller.util.constant.ProjectConstants;
import it.pagopa.interop.probing.caller.util.logging.Logger;


@Service
public class PollingResultSend {

  @Value("${amazon.sqs.end-point.poll-result-queue}")
  private String sqsUrl;

  @Autowired
  private AmazonSQS amazonSQS;

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private Logger logger;


  public void sendMessage(PollingDto polling) throws IOException {
    SendMessageRequest sendMessageRequest = new SendMessageRequest().withQueueUrl(sqsUrl)
        .withMessageBody(objectMapper.writeValueAsString(polling));
    amazonSQS.sendMessage(sendMessageRequest);
    logger.logMessagePushedToQueue(polling.eserviceRecordId(), ProjectConstants.SQS_POLLING_QUEUE,
        ProjectConstants.SERVICE_GROUP_ID);
  }
}
