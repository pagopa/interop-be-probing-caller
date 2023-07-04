package it.pagopa.interop.probing.caller.tracing.config;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import com.amazonaws.xray.AWSXRay;

@Aspect
@Component
public class XRayMDCConfig {

  @After("execution(* it.pagopa.interop.probing.caller.consumer.PollingReceiver.*(..))")
  public void afterListener(JoinPoint joinPoint) {
    AWSXRay.endSegment();
  }
}
