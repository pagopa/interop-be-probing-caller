package it.pagopa.interop.probing.caller.dto;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
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
  @Size(min = 1, max = 2048, message = "must be at least 1 and less than or equal to 2048")
  private String koReason;

  @JsonProperty("checkTime")
  @NotBlank(message = "must not be blank")
  @Pattern(regexp = "\\d+", message = "should be numeric")
  private String checkTime;

}
