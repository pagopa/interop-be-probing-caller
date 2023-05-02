package it.pagopa.interop.probing.caller.client.config;

import org.springframework.context.annotation.Configuration;
import feign.Feign;
import feign.Target;
import feign.hc5.ApacheHttp5Client;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import feign.slf4j.Slf4jLogger;
import it.pagopa.interop.probing.caller.client.FeignRestClient;
import it.pagopa.interop.probing.caller.client.FeignSoapClient;
import lombok.Getter;
import lombok.experimental.Accessors;

@Getter
@Accessors(fluent = true)
@Configuration
public class FeignClientConfig {

  FeignRestClient feignRestClient;
  FeignSoapClient feignSoapClient;

  public FeignClientConfig() {

    feignRestClient = Feign.builder().encoder(new JacksonEncoder()).decoder(new JacksonDecoder())
        .target(Target.EmptyTarget.create(FeignRestClient.class));

    feignSoapClient = Feign.builder().client(new ApacheHttp5Client())
        .logger(new Slf4jLogger(FeignSoapClient.class))
        .target(Target.EmptyTarget.create(FeignSoapClient.class));
  }



}
