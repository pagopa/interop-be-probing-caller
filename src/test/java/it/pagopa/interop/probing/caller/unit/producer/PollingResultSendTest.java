package it.pagopa.interop.probing.caller.unit.producer;

import java.io.IOException;
import java.time.OffsetDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.util.ReflectionTestUtils;
import com.amazonaws.services.sqs.AmazonSQSAsync;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.awspring.cloud.messaging.listener.SimpleMessageListenerContainer;
import it.pagopa.interop.probing.caller.dto.impl.PollingDto;
import it.pagopa.interop.probing.caller.producer.PollingResultSend;
import it.pagopa.interop.probing.caller.util.logging.Logger;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class PollingResultSendTest {

  @InjectMocks
  PollingResultSend pollingResultSend;

  @Mock
  private AmazonSQSAsync amazonSQS;

  @Mock
  private Logger logger;

  @MockBean
  private SimpleMessageListenerContainer simpleMessageListenerContainer;

  @Mock
  ObjectMapper mapper;

  private PollingDto pollingDto;
  private static final String TEST_URL = "http://queue/test-queue";

  @BeforeEach
  void setup() {
    ReflectionTestUtils.setField(pollingResultSend, "sqsUrl", TEST_URL);
    pollingDto =
        PollingDto.builder().eserviceRecordId(1L).responseReceived(OffsetDateTime.now()).build();
  }

  @Test
  @DisplayName("The sendMessage method of PollingResultSend class is tested.")
  void testSendMessage_whenGivenValidPollingDto_thenProducerWriteOnQueue() throws IOException {

    Mockito.when(amazonSQS.sendMessage(Mockito.any())).thenReturn(null);
    pollingResultSend.sendMessage(pollingDto);
    SendMessageRequest sendMessageRequest = new SendMessageRequest().withQueueUrl(TEST_URL)
        .withMessageBody(mapper.writeValueAsString(pollingDto));
    Mockito.verify(amazonSQS).sendMessage(sendMessageRequest);
  }



}
