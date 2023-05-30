package it.pagopa.interop.probing.caller.unit.consumer;

import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;
import java.io.IOException;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.awspring.cloud.messaging.listener.SimpleMessageListenerContainer;
import it.pagopa.interop.probing.caller.consumer.PollingReceiver;
import it.pagopa.interop.probing.caller.dto.impl.EserviceContentDto;
import it.pagopa.interop.probing.caller.dto.impl.PollingDto;
import it.pagopa.interop.probing.caller.dto.impl.TelemetryDto;
import it.pagopa.interop.probing.caller.producer.PollingResultSend;
import it.pagopa.interop.probing.caller.producer.TelemetryResultSend;
import it.pagopa.interop.probing.caller.util.ClientUtil;
import it.pagopa.interop.probing.caller.util.EserviceStatus;
import it.pagopa.interop.probing.caller.util.EserviceTechnology;
import it.pagopa.interop.probing.caller.util.logging.Logger;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class PollingReceiverTest {

  @InjectMocks
  PollingReceiver pollingReceiver;

  @Mock
  private ClientUtil clientUtil;

  @Mock
  PollingResultSend pollingResultSend;

  @Mock
  TelemetryResultSend telemetryResultSend;

  @Mock
  PollingDto.PollingDtoBuilder builderMock;

  @Mock
  ObjectMapper mapper;

  @Mock
  private Logger logger;

  @MockBean
  private SimpleMessageListenerContainer simpleMessageListenerContainer;


  private TelemetryDto telemetryDto;
  private PollingDto pollingDto;
  private EserviceContentDto eserviceContentDto;

  @BeforeEach
  void setup() {

    String[] audience = {"aud1", "aud2"};
    String[] basePath = {"basePath1", "basePath2"};
    eserviceContentDto = EserviceContentDto.builder().eserviceRecordId(1L)
        .technology(EserviceTechnology.REST).basePath(basePath).audience(audience).build();
    telemetryDto = TelemetryDto.builder().eserviceRecordId(1L).status(EserviceStatus.OK)
        .responseTime(12345L).koReason(null).checkTime("12345").build();
    pollingDto = PollingDto.builder().eserviceRecordId(1L)
        .responseReceived(OffsetDateTime.now(ZoneOffset.UTC)).status(EserviceStatus.OK).build();
  }

  @Test
  @DisplayName("Reads the message from the queue and pushes it to the polling and telemetry queues.")
  void testReceiverMessage_whenGivenValidMessage_thenMessageIsSentToQueue() throws IOException {

    try (MockedStatic<PollingDto> pollingBuilder = mockStatic(PollingDto.class);) {

      pollingBuilder.when(PollingDto::builder).thenReturn(builderMock);
      Mockito.when(builderMock.eserviceRecordId(Mockito.anyLong())).thenReturn(builderMock);
      Mockito.when(builderMock.responseReceived(Mockito.any(OffsetDateTime.class)))
          .thenReturn(builderMock);
      Mockito.when(builderMock.status(Mockito.any(EserviceStatus.class))).thenReturn(builderMock);

      Mockito.when(builderMock.build()).thenReturn(pollingDto);

      String message =
          "{\"eserviceRecordId\":1,\"technology\":\"REST\",\"basePath\":[\"path1\",\"path2\"],\"audience\":[\"aud1\",\"path2\"]}";

      Mockito.when(mapper.readValue(message, EserviceContentDto.class))
          .thenReturn(eserviceContentDto);

      Mockito.when(clientUtil.callProbing(eserviceContentDto)).thenReturn(telemetryDto);

      pollingReceiver.receiveStringMessage(message);

      verify(clientUtil).callProbing(eserviceContentDto);
      verify(telemetryResultSend).sendMessage(telemetryDto);
      verify(pollingResultSend).sendMessage(pollingDto);
    }

  }


}
