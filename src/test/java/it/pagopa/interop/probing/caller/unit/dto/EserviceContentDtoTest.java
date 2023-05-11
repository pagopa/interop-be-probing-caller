package it.pagopa.interop.probing.caller.unit.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import java.io.IOException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import io.awspring.cloud.messaging.listener.SimpleMessageListenerContainer;
import it.pagopa.interop.probing.caller.dto.impl.EserviceContentDto;
import it.pagopa.interop.probing.caller.util.EserviceTechnology;


@SpringBootTest
@ExtendWith(MockitoExtension.class)
class EserviceContentDtoTest {

  @MockBean
  private SimpleMessageListenerContainer simpleMessageListenerContainer;

  private EserviceContentDto eserviceContentDto;

  @BeforeEach
  void setup() throws IOException {
    String[] basePath = {"basePath1", "basePath2"};
    eserviceContentDto = EserviceContentDto.builder().eserviceRecordId(1L)
        .technology(EserviceTechnology.REST).basePath(basePath).build();
  }


  @Test
  @DisplayName("Test the utility toString of lombok.")
  void testToString_whenGivenValidEserviceContentDto_thenValidEquals() throws IOException {
    String serviceString =
        "EserviceContentDto(eserviceRecordId=1, technology=REST, basePath=[basePath1, basePath2])";
    assertEquals(eserviceContentDto.toString(), serviceString);
  }

}
