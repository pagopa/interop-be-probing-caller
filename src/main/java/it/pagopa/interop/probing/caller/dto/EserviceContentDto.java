package it.pagopa.interop.probing.caller.dto;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.pagopa.interop.probing.caller.annotations.ValidateStringArraySize;
import it.pagopa.interop.probing.caller.util.EserviceTechnology;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true, fluent = true)
public class EserviceContentDto {

  @JsonProperty("eserviceRecordId")
  @NotNull(message = "must not be null")
  @Min(value = 1, message = "must be at least 1")
  private Long eserviceRecordId;

  @JsonProperty("technology")
  @NotNull(message = "must not be null")
  private EserviceTechnology technology;

  @JsonProperty("basePath")
  @NotEmpty(message = "list cannot be empty")
  @ValidateStringArraySize(maxSize = 2048)
  private String[] basePath;


}
