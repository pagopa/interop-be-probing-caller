package it.pagopa.interop.probing.caller.config.client;

import org.springframework.context.annotation.Configuration;
import feign.Feign;
import feign.Logger.Level;
import feign.Target;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import feign.slf4j.Slf4jLogger;
import it.pagopa.interop.probing.caller.client.FeignRestClient;
import lombok.Getter;
import lombok.experimental.Accessors;

@Getter
@Accessors(fluent = true)
@Configuration
public class RestClientConfig {

  FeignRestClient feignRestClient;

  public RestClientConfig() {
    feignRestClient = Feign.builder().encoder(new JacksonEncoder()).decoder(new JacksonDecoder())
        .logger(new Slf4jLogger()).logLevel(Level.FULL)
        .target(Target.EmptyTarget.create(FeignRestClient.class));

  }
}
