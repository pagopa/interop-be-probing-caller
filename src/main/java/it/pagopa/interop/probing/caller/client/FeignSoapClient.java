package it.pagopa.interop.probing.caller.client;

import java.net.URI;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpHeaders;
import feign.Headers;
import feign.Param;
import feign.RequestLine;
import it.pagopa.interop.probing.caller.soap.probing.ProbingRequest;
import it.pagopa.interop.probing.caller.soap.probing.ProbingResponse;

@FeignClient(name = "feignSoapClient")
public interface FeignSoapClient {

  @RequestLine("POST")
  @Headers({"SOAPAction: probing/v1", "Content-Type: text/xml;charset=UTF-8",
      "Authorization:Bearer {Authorization}", "Accept: text/xml"})
  ProbingResponse probing(URI url, ProbingRequest body,
      @Param(HttpHeaders.AUTHORIZATION) String authorizationHeader);
}
