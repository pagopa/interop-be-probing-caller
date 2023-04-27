package it.pagopa.interop.probing.caller.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import it.pagopa.interop.probing.caller.util.EserviceTechnology;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EserviceContent {

  @JsonProperty("id")
  private String id;

  @JsonProperty("technology")
  private EserviceTechnology technology;

  @JsonProperty("basePath")
  private String[] basePath;
}
