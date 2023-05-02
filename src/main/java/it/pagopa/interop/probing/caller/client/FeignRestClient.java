package it.pagopa.interop.probing.caller.client;

import java.net.URI;
import org.springframework.cloud.openfeign.FeignClient;
import feign.RequestLine;
import feign.Response;

@FeignClient(name = "feignRestClient")
public interface FeignRestClient {

  @RequestLine(value = "GET")
  Response probing(URI baseUrl);
}
