package it.pagopa.interop.probing.caller.config.sqs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.services.sqs.AmazonSQSAsync;
import com.amazonaws.services.sqs.AmazonSQSAsyncClientBuilder;
import com.amazonaws.xray.AWSXRay;
import com.amazonaws.xray.handlers.TracingHandler;

@Configuration
public class SqsConfig {


  @Bean
  @Primary
  public AmazonSQSAsync amazonSQSAsync() {
    return AmazonSQSAsyncClientBuilder.standard()
        .withCredentials(new DefaultAWSCredentialsProviderChain())
        .withRequestHandlers(new TracingHandler(AWSXRay.getGlobalRecorder())).build();

  }

}
