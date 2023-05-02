package it.pagopa.interop.probing.caller.dto;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.pagopa.interop.probing.caller.util.EserviceStatus;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Builder
@Accessors(chain = true, fluent = true)
public class TelemetryDto {

  @JsonProperty("eserviceRecordId")
  @NotNull(message = "must not be null")
  @Min(value = 1, message = "must be at least 1")
  private Long eserviceRecordId;

  @JsonProperty("status")
  @NotNull(message = "must not be null")
  private EserviceStatus status;

  @JsonProperty("responseTime")
  @NotNull(message = "must not be null")
  private Long responseTime;

  @JsonProperty("koReason")
  private String koReason;

  @JsonProperty("checkTime")
  @NotNull(message = "must not be null")
  private String checkTime;

}
