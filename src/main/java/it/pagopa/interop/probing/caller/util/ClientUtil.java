package it.pagopa.interop.probing.caller.util;

import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Response;
import it.pagopa.interop.probing.caller.config.client.RestClientConfig;
import it.pagopa.interop.probing.caller.config.client.SoapClientConfig;
import it.pagopa.interop.probing.caller.dto.impl.EserviceContentDto;
import it.pagopa.interop.probing.caller.dto.impl.TelemetryDto;
import it.pagopa.interop.probing.caller.dtos.Problem;
import it.pagopa.interop.probing.caller.soap.probing.ObjectFactory;
import it.pagopa.interop.probing.caller.soap.probing.ProbingResponse;
import it.pagopa.interop.probing.caller.util.logging.Logger;

@Component
public class ClientUtil {

  @Autowired
  RestClientConfig restClientConfig;

  @Autowired
  SoapClientConfig soapClientConfig;

  @Autowired
  private Logger logger;

  public TelemetryDto callProbing(EserviceContentDto service) {

    TelemetryDto telemetryResult =
        TelemetryDto.builder().eserviceRecordId(service.eserviceRecordId()).build();

    logger.logMessageCallProbing(service.technology().getValue(), service.basePath()[0]);

    try {

      if (service.technology().equals(EserviceTechnology.REST)) {
        telemetryResult = callRest(telemetryResult, service);
      } else {
        telemetryResult = callSoap(telemetryResult, service);
      }

    } catch (Exception e) {
      logger.logMessageException(e);
      telemetryResult.status(EserviceStatus.KO).koReason(e.getMessage());
    }

    logger.logMessageResponseCallProbing(telemetryResult);

    return telemetryResult;
  }


  private TelemetryDto callRest(TelemetryDto telemetryResult, EserviceContentDto service)
      throws IOException {
    long before = System.currentTimeMillis();
    Response response =
        restClientConfig.feignRestClient().probing(URI.create(service.basePath()[0]));
    long elapsedTime = System.currentTimeMillis() - before;
    logger.logResultCallProbing(response.status(), response.toString(), elapsedTime);
    return receiverResponse(response.status(), telemetryResult, decodeReason(response, elapsedTime),
        elapsedTime, before);
  }

  private TelemetryDto callSoap(TelemetryDto telemetryResult, EserviceContentDto service) {
    ObjectFactory o = new ObjectFactory();
    long before = System.currentTimeMillis();
    ProbingResponse response = soapClientConfig.feignSoapClient()
        .probing(URI.create(service.basePath()[0]), o.createProbingRequest());
    long elapsedTime = System.currentTimeMillis() - before;
    logger.logResultCallProbing(Integer.valueOf(response.getStatus()), response.toString(),
        elapsedTime);
    return receiverResponse(Integer.valueOf(response.getStatus()), telemetryResult,
        response.getDescription(), elapsedTime, before);
  }

  private TelemetryDto receiverResponse(int status, TelemetryDto telemetryResult, String koReason,
      long elapsedTime, long before) {
    telemetryResult.checkTime(String.valueOf(before));
    if (HttpStatus.valueOf(status).is2xxSuccessful()) {
      return telemetryResult.status(EserviceStatus.OK).responseTime(elapsedTime);
    } else {
      return telemetryResult.status(EserviceStatus.KO).koReason(koReason);
    }
  }

  private String decodeReason(Response response, long elapsedTime) throws IOException {
    HttpStatus status = HttpStatus.valueOf(response.status());
    String reason = null;
    if (status.is4xxClientError() || status.is3xxRedirection()) {
      reason = response.reason();
      logger.logResultCallProbing(response.status(), reason, elapsedTime);
    }


    if (HttpStatus.valueOf(response.status()).is5xxServerError()) {
      Problem problem = new ObjectMapper()
          .readValue(response.body().asReader(StandardCharsets.UTF_8), Problem.class);
      reason = problem.getDetail();
      logger.logResultCallProbing(response.status(), problem.toString(), elapsedTime);
    }
    return reason;
  }
}
