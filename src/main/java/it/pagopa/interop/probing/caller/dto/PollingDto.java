package it.pagopa.interop.probing.caller.dto;

import java.time.OffsetDateTime;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Builder
@Accessors(chain = true, fluent = true)
public class PollingDto {

  @JsonProperty("eserviceRecordId")
  @NotNull(message = "must not be null")
  @Min(value = 1, message = "must be at least 1")
  private Long eserviceRecordId;

  @JsonProperty("responseTime")
  @NotNull(message = "must not be null")
  private OffsetDateTime responseTime;

}
