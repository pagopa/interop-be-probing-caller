package it.pagopa.interop.probing.caller.client;

import java.net.URI;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpHeaders;
import feign.Headers;
import feign.Param;
import feign.RequestLine;
import feign.Response;

@FeignClient(name = "feignRestClient")
public interface FeignRestClient {

  @RequestLine("GET")
  @Headers({"Content-Type: application/json", "Accept:application/json",
      "Authorization:Bearer {Authorization}"})
  Response probing(URI baseUrl, @Param(HttpHeaders.AUTHORIZATION) String authorization);
}
