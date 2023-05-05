package it.pagopa.interop.probing.caller.client;

import java.net.URI;
import org.springframework.cloud.openfeign.FeignClient;
import feign.Headers;
import feign.RequestLine;
import feign.Response;
import it.pagopa.interop.probing.caller.soap.ws.client.Request;

@FeignClient(name = "feignSoapClient")
public interface FeignSoapClient {

  @RequestLine("POST")
  @Headers({"SOAPAction: probing", "Content-Type: text/xml;charset=UTF-8", "Accept: text/xml"})
  Response probing(URI url, Request body);
}
