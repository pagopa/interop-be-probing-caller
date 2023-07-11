package it.pagopa.interop.probing.caller.tracing.config;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.conn.HttpClientConnectionManager;
import org.springframework.cloud.commons.httpclient.ApacheHttpClientFactory;
import org.springframework.cloud.openfeign.support.FeignHttpClientProperties;
import org.springframework.context.annotation.Bean;
import com.amazonaws.xray.proxies.apache.http.HttpClientBuilder;
import feign.Client;
import feign.httpclient.ApacheHttpClient;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class XRayFeignClientConfig {

  @Bean
  public Client client(ApacheHttpClientFactory httpClientFactory,
      HttpClientConnectionManager httpClientConnectionManager,
      FeignHttpClientProperties httpClientProperties) {
    RequestConfig defaultRequestConfig =
        RequestConfig.custom().setConnectTimeout(httpClientProperties.getConnectionTimeout())
            .setRedirectsEnabled(httpClientProperties.isFollowRedirects()).build();
    log.debug("Setting up aws xray Client bean for FeignClient.");
    return new ApacheHttpClient(
        HttpClientBuilder.create().setConnectionManager(httpClientConnectionManager)
            .setDefaultRequestConfig(defaultRequestConfig).build());
  }

}
