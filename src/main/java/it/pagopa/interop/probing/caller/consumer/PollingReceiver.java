package it.pagopa.interop.probing.caller.consumer;

import java.io.IOException;
import java.net.URI;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Response;
import io.awspring.cloud.messaging.listener.SqsMessageDeletionPolicy;
import io.awspring.cloud.messaging.listener.annotation.SqsListener;
import it.pagopa.interop.probing.caller.client.config.FeignClientConfig;
import it.pagopa.interop.probing.caller.dto.EserviceContentDto;
import it.pagopa.interop.probing.caller.dto.TelemetryDto;
import it.pagopa.interop.probing.caller.producer.PollingResultSend;
import it.pagopa.interop.probing.caller.producer.TelemetryResultSend;
import it.pagopa.interop.probing.caller.util.EserviceStatus;
import it.pagopa.interop.probing.caller.util.EserviceTechnology;
import it.pagopa.interop.probing.caller.util.ProjectConstants;
import it.pagopa.interop.probing.caller.util.logging.Logger;


@Component
public class PollingReceiver {

  @Autowired
  ObjectMapper mapper;

  @Autowired
  PollingResultSend pollingResultSend;

  @Autowired
  TelemetryResultSend telemetryResultSend;

  @Autowired
  FeignClientConfig feignClientConfig;

  @Autowired
  private Logger logger;

  @SqsListener(value = "${amazon.sqs.end-point.poll-queue}",
      deletionPolicy = SqsMessageDeletionPolicy.ON_SUCCESS)
  public void receiveStringMessage(final String message) throws IOException {
    EserviceContentDto service = mapper.readValue(message, EserviceContentDto.class);
    try {
      telemetryResultSend.sendMessage(callProbing(service));
      pollingResultSend.sendMessage(service);
    } catch (IOException e) {
      logger.logMessageException(e);
      throw e;
    }
  }

  private TelemetryDto callProbing(EserviceContentDto service) {
    TelemetryDto telemetryResult =
        TelemetryDto.builder().eserviceRecordId(service.eserviceRecordId()).build();
    long before = System.currentTimeMillis();

    Response response = null;
    try {
      if (service.technology().equals(EserviceTechnology.REST)) {
        response = feignClientConfig.feignRestClient().probing(
            URI.create(Objects.nonNull(service.basePath()) ? service.basePath()[0] : null));
      } else {
        response =
            feignClientConfig.feignSoapClient().probing(URI.create(service.basePath()[0]), null);
      }
      if (HttpStatus.NO_CONTENT == HttpStatus.valueOf(response.status())) {
        telemetryResult.status(EserviceStatus.OK);
      } else {
        telemetryResult.status(EserviceStatus.KO).koReason(response.reason());
      }
    } catch (Exception e) {
      logger.logMessageException(e);
      telemetryResult.status(EserviceStatus.KO).koReason(ProjectConstants.GENERIC_EXCEPTION);
    }
    long elapsedTime = System.currentTimeMillis() - before;
    return telemetryResult.responseTime(elapsedTime).checkTime(String.valueOf(before));
  }

}
