package it.pagopa.interop.probing.caller.dto.impl;

import java.time.OffsetDateTime;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.pagopa.interop.probing.caller.dto.Dto;
import it.pagopa.interop.probing.caller.util.EserviceStatus;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Builder
@Accessors(chain = true, fluent = true)
public class PollingDto implements Dto {

  private static final long serialVersionUID = 1L;

  @JsonProperty("eserviceRecordId")
  @NotNull(message = "must not be null")
  @Min(value = 1, message = "must be at least 1")
  private Long eserviceRecordId;

  @JsonProperty("responseTime")
  @NotNull(message = "must not be null")
  private OffsetDateTime responseReceived;

  @JsonProperty("status")
  @NotNull(message = "must not be null")
  private EserviceStatus status;

}
