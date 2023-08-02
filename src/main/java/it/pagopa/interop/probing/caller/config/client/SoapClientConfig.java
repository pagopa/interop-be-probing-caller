package it.pagopa.interop.probing.caller.config.client;

import org.springframework.context.annotation.Configuration;
import feign.Feign;
import feign.Retryer;
import feign.Target;
import feign.jaxb.JAXBContextFactory;
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

    feignSoapClient =
        Feign.builder().encoder(new SOAPEncoder(jaxbFactory)).decoder(new SOAPDecoder(jaxbFactory))
            .retryer(Retryer.NEVER_RETRY).target(Target.EmptyTarget.create(FeignSoapClient.class));
  }
}
