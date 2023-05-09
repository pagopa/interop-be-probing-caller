package it.pagopa.interop.probing.caller.util;

import java.io.IOException;
import java.net.URI;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Response;
import it.pagopa.interop.probing.caller.config.client.RestClientConfig;
import it.pagopa.interop.probing.caller.config.client.SoapClientConfig;
import it.pagopa.interop.probing.caller.dto.EserviceContentDto;
import it.pagopa.interop.probing.caller.dto.TelemetryDto;
import it.pagopa.interop.probing.caller.dtos.Problem;
import it.pagopa.interop.probing.caller.soap.probing.ObjectFactory;
import it.pagopa.interop.probing.caller.soap.probing.ProbingResponse;
import it.pagopa.interop.probing.caller.util.constant.ProjectConstants;
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

    long before = System.currentTimeMillis();
    telemetryResult.checkTime(String.valueOf(before));
    try {

      if (service.technology().equals(EserviceTechnology.REST)) {
        telemetryResult = callRest(telemetryResult, service, before);
      } else {
        telemetryResult = callSoap(telemetryResult, service, before);
      }

    } catch (Exception e) {
      logger.logMessageException(e);
      telemetryResult.status(EserviceStatus.KO).koReason(ProjectConstants.GENERIC_EXCEPTION);
    }

    logger.logMessageResponseCallProbing(telemetryResult);

    return telemetryResult;
  }

  private TelemetryDto callRest(TelemetryDto telemetryResult, EserviceContentDto service,
      long before) throws IOException {
    Response response = restClientConfig.feignRestClient()
        .probing(URI.create(Objects.nonNull(service.basePath()) ? service.basePath()[0] : null));
    Problem problem = new ObjectMapper().readValue(response.body().toString(), Problem.class);
    logger.logResultCallProbing(response.status(), response.body().toString());
    return receiverResponse(response.status(), telemetryResult, problem.getDetail(), before);
  }

  private TelemetryDto callSoap(TelemetryDto telemetryResult, EserviceContentDto service,
      long before) {
    ObjectFactory o = new ObjectFactory();
    ProbingResponse response = soapClientConfig.feignSoapClient().probing(
        URI.create(Objects.nonNull(service.basePath()) ? service.basePath()[0] : null),
        o.createProbingRequest());

    return receiverResponse(Integer.valueOf(response.getStatus()), telemetryResult, null, before);
  }

  private TelemetryDto receiverResponse(int status, TelemetryDto telemetryResult, String koReason,
      long before) {
    if (HttpStatus.valueOf(status).is2xxSuccessful()) {
      long elapsedTime = System.currentTimeMillis() - before;
      return telemetryResult.status(EserviceStatus.OK).responseTime(elapsedTime);
    } else {
      return telemetryResult.status(EserviceStatus.KO).koReason(koReason);
    }
  }
}
