package it.pagopa.interop.probing.caller.config.kms;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.services.kms.AWSKMS;
import com.amazonaws.services.kms.AWSKMSClientBuilder;

@Configuration
public class KmsConfig {

  @Bean
  @Primary
  public AWSKMS amazonKMS() {
    return AWSKMSClientBuilder.standard().withCredentials(new DefaultAWSCredentialsProviderChain())
        .build();
  }
}
