package it.pagopa.interop.probing.caller.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import it.pagopa.interop.probing.caller.util.EserviceStatus;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TelemetryDto {

  @JsonProperty("serviceName")
  private String serviceName;

  @JsonProperty("status")
  private EserviceStatus status;

  @JsonProperty("responseTime")
  private Integer responseTime;

  @JsonProperty("koReason")
  private String koReason;

  @JsonProperty("checkTime")
  private String checkTime;

}
