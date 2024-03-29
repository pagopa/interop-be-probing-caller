package it.pagopa.interop.probing.caller.tracing.config;

import javax.servlet.Filter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import com.amazonaws.xray.javax.servlet.AWSXRayServletFilter;
import com.amazonaws.xray.proxies.apache.http.HttpClientBuilder;

@Configuration
@Primary
public class TracingConfig {

  @Value("${spring.application.name}")
  private String awsXraySegmentName;

  @Bean
  public Filter tracingFilter() {
    return new AWSXRayServletFilter(awsXraySegmentName);
  }

  @Bean
  public HttpClientBuilder xrayHttpClientBuilder() {

    return HttpClientBuilder.create();
  }
}
