package it.pagopa.interop.probing.caller.unit.producer;

import java.io.IOException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.util.ReflectionTestUtils;
import com.amazonaws.services.sqs.AmazonSQSAsync;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.awspring.cloud.messaging.listener.SimpleMessageListenerContainer;
import it.pagopa.interop.probing.caller.dto.impl.TelemetryDto;
import it.pagopa.interop.probing.caller.producer.TelemetryResultSend;
import it.pagopa.interop.probing.caller.util.EserviceStatus;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class TelemetryResultSendTest {

  @InjectMocks
  @Autowired
  private TelemetryResultSend telemetryResultSend;

  @Mock
  private AmazonSQSAsync amazonSQS;

  @MockBean
  private SimpleMessageListenerContainer simpleMessageListenerContainer;

  @Mock
  private ObjectMapper mapper;

  private TelemetryDto telemetryDto;
  private static final String TEST_URL = "http://queue/test-queue";

  @BeforeEach
  void setup() {
    ReflectionTestUtils.setField(telemetryResultSend, "sqsUrl", TEST_URL);
    telemetryDto = TelemetryDto.builder().eserviceRecordId(1L).status(EserviceStatus.OK)
        .responseTime(12345L).koReason(null).checkTime("12345").build();
  }


  @Test
  @DisplayName("Sent message on the telemetry queue.")
  void testSendMessage_whenGivenValidTelemetryDto_thenProducerWriteOnQueue() throws IOException {

    Mockito.when(amazonSQS.sendMessage(Mockito.any())).thenReturn(null);
    telemetryResultSend.sendMessage(telemetryDto);
    SendMessageRequest sendMessageRequest = new SendMessageRequest().withQueueUrl(TEST_URL)
        .withMessageBody(mapper.writeValueAsString(telemetryDto));
    Mockito.verify(amazonSQS).sendMessage(sendMessageRequest);
  }



}
