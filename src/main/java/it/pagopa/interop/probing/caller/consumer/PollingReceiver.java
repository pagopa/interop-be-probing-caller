package it.pagopa.interop.probing.caller.consumer;

import java.io.IOException;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.xray.AWSXRay;
import com.amazonaws.xray.AWSXRayRecorderBuilder;
import com.amazonaws.xray.entities.TraceHeader;
import com.amazonaws.xray.spring.aop.XRayEnabled;
import com.amazonaws.xray.strategy.sampling.DefaultSamplingStrategy;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.awspring.cloud.messaging.listener.Acknowledgment;
import io.awspring.cloud.messaging.listener.SqsMessageDeletionPolicy;
import io.awspring.cloud.messaging.listener.annotation.SqsListener;
import it.pagopa.interop.probing.caller.dto.impl.EserviceContentDto;
import it.pagopa.interop.probing.caller.dto.impl.PollingDto;
import it.pagopa.interop.probing.caller.dto.impl.TelemetryDto;
import it.pagopa.interop.probing.caller.producer.PollingResultSend;
import it.pagopa.interop.probing.caller.producer.TelemetryResultSend;
import it.pagopa.interop.probing.caller.util.ClientUtil;
import it.pagopa.interop.probing.caller.util.logging.Logger;
import it.pagopa.interop.probing.caller.util.logging.LoggingPlaceholders;


@Component
@XRayEnabled
public class PollingReceiver {

  @Autowired
  ObjectMapper mapper;

  @Autowired
  private PollingResultSend pollingResultSend;

  @Autowired
  private TelemetryResultSend telemetryResultSend;

  @Autowired
  private Logger logger;

  @Autowired
  private ClientUtil clientUtil;

  @Value("${spring.application.name}")
  private String awsXraySegmentName;


  @Async
  @SqsListener(value = "${amazon.sqs.end-point.poll-queue}",
      deletionPolicy = SqsMessageDeletionPolicy.NEVER)
  public void receiveStringMessage(final Message messageFull, final String message,
      @Headers MessageHeaders headers, Acknowledgment acknowledgment) throws IOException {

    AWSXRayRecorderBuilder builder = AWSXRayRecorderBuilder.standard().withDefaultPlugins()
        .withSamplingStrategy(new DefaultSamplingStrategy());

    AWSXRay.setGlobalRecorder(builder.build());

    String traceHeaderStr = messageFull.getAttributes().get("AWSTraceHeader");
    TraceHeader traceHeader = TraceHeader.fromString(traceHeaderStr);
    if (AWSXRay.getCurrentSegmentOptional().isEmpty()) {
      AWSXRay.getGlobalRecorder().beginSegment(awsXraySegmentName, traceHeader.getRootTraceId(),
          null);
    }
    MDC.put(LoggingPlaceholders.TRACE_ID_XRAY_PLACEHOLDER,
        LoggingPlaceholders.TRACE_ID_XRAY_MDC_PREFIX
            + AWSXRay.getCurrentSegment().getTraceId().toString() + "]");
    String threadId = Thread.currentThread().getId() + "-" + Thread.currentThread().getName();

    EserviceContentDto service = mapper.readValue(message, EserviceContentDto.class);

    try {
      TelemetryDto telemetryDto = clientUtil.callProbing(service);
      telemetryResultSend.sendMessage(telemetryDto);

      pollingResultSend
          .sendMessage(PollingDto.builder().eserviceRecordId(service.eserviceRecordId())
              .responseReceived(OffsetDateTime.now(ZoneOffset.UTC)).status(telemetryDto.status())
              .build());

      acknowledgment.acknowledge();

      logger.logMessageReceiver(service.eserviceRecordId(), threadId);
      AWSXRay.endSegment();
    } catch (IOException e) {
      logger.logMessageException(e);
      throw e;
    }
    AWSXRay.endSegment();
  }
}
