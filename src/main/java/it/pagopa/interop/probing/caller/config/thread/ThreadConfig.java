package it.pagopa.interop.probing.caller.config.thread;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
@EnableAsync
public class ThreadConfig implements AsyncConfigurer {

  @Value("${threadpool.max-pool-size}")
  private int maxPoolSize;

  @Value("${threadpool.queue-capacity}")
  private int queueCapacity;

  @Value("${threadpool.core-pool-size}")
  private int corePoolSize;

  @Value("${threadpool.thread-name-prefix}")
  private String threadNamePrefix;

  @Override
  @Bean(name = "threadPoolTaskExecutor")
  public ThreadPoolTaskExecutor getAsyncExecutor() {
    ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
    executor.setCorePoolSize(corePoolSize);
    executor.setMaxPoolSize(maxPoolSize);
    executor.setQueueCapacity(queueCapacity);
    executor.setThreadNamePrefix(threadNamePrefix);
    executor.setRejectedExecutionHandler(new BlockingTaskSubmissionPolicy());
    executor.initialize();
    return executor;
  }

}
