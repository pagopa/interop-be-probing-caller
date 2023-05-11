package it.pagopa.interop.probing.caller.unit.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import java.io.IOException;
import java.net.URI;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.Resource;
import org.springframework.util.StreamUtils;
import feign.Request;
import feign.RequestTemplate;
import feign.Response;
import io.awspring.cloud.messaging.listener.SimpleMessageListenerContainer;
import it.pagopa.interop.probing.caller.client.FeignRestClient;
import it.pagopa.interop.probing.caller.client.FeignSoapClient;
import it.pagopa.interop.probing.caller.config.client.RestClientConfig;
import it.pagopa.interop.probing.caller.config.client.SoapClientConfig;
import it.pagopa.interop.probing.caller.dto.impl.EserviceContentDto;
import it.pagopa.interop.probing.caller.dto.impl.TelemetryDto;
import it.pagopa.interop.probing.caller.soap.probing.ProbingRequest;
import it.pagopa.interop.probing.caller.soap.probing.ProbingResponse;
import it.pagopa.interop.probing.caller.util.ClientUtil;
import it.pagopa.interop.probing.caller.util.EserviceStatus;
import it.pagopa.interop.probing.caller.util.EserviceTechnology;
import it.pagopa.interop.probing.caller.util.logging.Logger;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class ClientUtilTest {

  @Mock
  RestClientConfig restClientConfig;

  @Mock
  SoapClientConfig soapClientConfig;

  @Mock
  FeignRestClient feignRestClient;

  @Mock
  FeignSoapClient feignSoapClient;

  @Mock
  private Logger logger;

  @MockBean
  private SimpleMessageListenerContainer simpleMessageListenerContainer;

  @InjectMocks
  ClientUtil clientUtil;

  @Value("classpath:data/bodyContent.json")
  private Resource bodyContent;

  private EserviceContentDto eserviceContentRestDto;
  private EserviceContentDto eserviceContentSoapDto;
  private TelemetryDto telemetryDto;
  private TelemetryDto telemetryDtoKO;

  @BeforeEach
  void setup() {

    String[] basePath = {"basePath1", "basePath2"};
    eserviceContentRestDto = EserviceContentDto.builder().eserviceRecordId(1L)
        .technology(EserviceTechnology.REST).basePath(basePath).build();
    eserviceContentSoapDto = EserviceContentDto.builder().eserviceRecordId(1L)
        .technology(EserviceTechnology.SOAP).basePath(basePath).build();
    telemetryDto = TelemetryDto.builder().eserviceRecordId(1L).status(EserviceStatus.OK)
        .responseTime(12345L).koReason(null).checkTime("12345").build();
    telemetryDtoKO = TelemetryDto.builder().eserviceRecordId(1L).status(EserviceStatus.KO)
        .koReason("KO").checkTime("12345").build();
  }

  @Test
  @DisplayName("Test call probing - rest")
  void testCallProbing_whenGivenEserviceWithRestTechnology_thenCallRestService()
      throws IOException {

    String body = getStringFromResourse(bodyContent);
    Request.Body requestBody = Request.Body.create(body, Charset.forName("UTF-8"));

    RequestTemplate requestTemplate = new RequestTemplate();
    requestTemplate.method(Request.HttpMethod.POST);
    requestTemplate.uri("/api/users");
    requestTemplate.headers(new HashMap<>());
    requestTemplate.bodyTemplate("${body}");

    Map<String, Object> variables = new HashMap<>();
    variables.put("body", requestBody);

    Request request = requestTemplate.resolve(variables).request();

    Response response = Response.builder().request(request).status(200).reason("OK")
        .body("{\"status\":\"200\"}", Charset.defaultCharset()).build();

    Mockito.when(restClientConfig.feignRestClient()).thenReturn(feignRestClient);
    Mockito.when(feignRestClient.probing(Mockito.any(URI.class))).thenReturn(response);

    TelemetryDto telemetryResult = clientUtil.callProbing(eserviceContentRestDto);
    assertEquals(telemetryDto.eserviceRecordId(), telemetryResult.eserviceRecordId());
    assertEquals(telemetryDto.status(), telemetryResult.status());
    assertNotNull(telemetryResult.responseTime());

  }

  @Test
  @DisplayName("Test OK call probing - soap")
  void testCallProbing_whenGivenEserviceWithRestTechnology_thenCallSoapServiceWithReturnOK()
      throws Exception {

    ProbingResponse soapResponse = new ProbingResponse();
    soapResponse.setStatus("200");

    Mockito.when(soapClientConfig.feignSoapClient()).thenReturn(feignSoapClient);
    Mockito.when(feignSoapClient.probing(Mockito.any(URI.class), Mockito.any(ProbingRequest.class)))
        .thenReturn(soapResponse);

    TelemetryDto telemetryResult = clientUtil.callProbing(eserviceContentSoapDto);

    assertEquals(telemetryDto.eserviceRecordId(), telemetryResult.eserviceRecordId());
    assertEquals(telemetryDto.status(), telemetryResult.status());
    assertNotNull(telemetryResult.responseTime());
  }


  @Test
  @DisplayName("Test KO call probing - soap")
  void testCallProbing_whenGivenEserviceWithRestTechnology_thenCallSoapServiceWithReturnKO()
      throws Exception {

    ProbingResponse soapResponse = new ProbingResponse();
    soapResponse.setStatus("500");

    Mockito.when(soapClientConfig.feignSoapClient()).thenReturn(feignSoapClient);
    Mockito.when(feignSoapClient.probing(Mockito.any(URI.class), Mockito.any(ProbingRequest.class)))
        .thenReturn(soapResponse);

    TelemetryDto telemetryResult = clientUtil.callProbing(eserviceContentSoapDto);

    assertEquals(telemetryDtoKO.eserviceRecordId(), telemetryResult.eserviceRecordId());
    assertEquals(telemetryDtoKO.status(), telemetryResult.status());
  }


  private static String getStringFromResourse(Resource resource) throws IOException {
    return StreamUtils.copyToString(resource.getInputStream(), Charset.defaultCharset());
  }

}

