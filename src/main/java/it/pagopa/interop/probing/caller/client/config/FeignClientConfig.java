package it.pagopa.interop.probing.caller.client.config;

import org.springframework.context.annotation.Configuration;
import feign.Feign;
import feign.Target;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import feign.jaxb.JAXBContextFactory;
import feign.soap.SOAPDecoder;
import feign.soap.SOAPEncoder;
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

    JAXBContextFactory jaxbFactory =
        new JAXBContextFactory.Builder().withMarshallerJAXBEncoding("UTF-8").build();

    feignRestClient = Feign.builder().encoder(new JacksonEncoder()).decoder(new JacksonDecoder())
        .target(Target.EmptyTarget.create(FeignRestClient.class));


    feignSoapClient =
        Feign.builder().encoder(new SOAPEncoder(jaxbFactory)).decoder(new SOAPDecoder(jaxbFactory))
            .target(Target.EmptyTarget.create(FeignSoapClient.class));
  }



}
