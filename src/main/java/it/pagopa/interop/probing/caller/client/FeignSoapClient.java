package it.pagopa.interop.probing.caller.client;

import java.net.URI;
import org.springframework.cloud.openfeign.FeignClient;
import feign.Headers;
import feign.RequestLine;
import it.pagopa.interop.probing.caller.soap.probing.ProbingRequest;
import it.pagopa.interop.probing.caller.soap.probing.ProbingResponse;

@FeignClient(name = "feignSoapClient")
public interface FeignSoapClient {

  @RequestLine("POST")
  @Headers({"SOAPAction: probing", "Content-Type: text/xml;charset=UTF-8", "Accept: text/xml"})
  ProbingResponse probing(URI url, ProbingRequest body);
}
