package it.pagopa.interop.probing.caller.config.thread;

import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

public class BlockingTaskSubmissionPolicy implements RejectedExecutionHandler {
  @Override
  public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
    if (!executor.getQueue().offer(r)) {
      new ThreadPoolExecutor.CallerRunsPolicy();
    }
  }
}
