package it.pagopa.interop.probing.caller.config.client;

import org.springframework.context.annotation.Configuration;
import feign.Feign;
import feign.Logger.Level;
import feign.Target;
import feign.jaxb.JAXBContextFactory;
import feign.slf4j.Slf4jLogger;
import feign.soap.SOAPDecoder;
import feign.soap.SOAPEncoder;
import it.pagopa.interop.probing.caller.client.FeignSoapClient;
import lombok.Getter;
import lombok.experimental.Accessors;

@Getter
@Accessors(fluent = true)
@Configuration
public class SoapClientConfig {

  FeignSoapClient feignSoapClient;

  public SoapClientConfig() {

    JAXBContextFactory jaxbFactory =
        new JAXBContextFactory.Builder().withMarshallerJAXBEncoding("UTF-8").build();

    feignSoapClient = Feign.builder().encoder(new SOAPEncoder(jaxbFactory))
        .decoder(new SOAPDecoder(jaxbFactory)).logger(new Slf4jLogger()).logLevel(Level.FULL)
        .target(Target.EmptyTarget.create(FeignSoapClient.class));
  }
}
