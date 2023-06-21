package it.pagopa.interop.probing.caller.tracing.config;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import com.amazonaws.xray.AWSXRay;

@Aspect
@Component
public class XRayMDCConfig {

  // @Before("execution(* it.pagopa.interop.probing.caller.consumer..*(..))")
  // public void beforeController(JoinPoint joinPoint) {
  // MDC.put(LoggingPlaceholders.TRACE_ID_XRAY_PLACEHOLDER,
  // LoggingPlaceholders.TRACE_ID_XRAY_MDC_PREFIX
  // + AWSXRay.getCurrentSegment().getTraceId().toString() + "]");
  // }


  // @Before("execution(* it.pagopa.interop.probing.caller.consumer.PollingReceiver.*(..))")
  // public void beforeListener(JoinPoint joinPoint) {
  // AWSXRay.beginSegment("caller");
  // }

  @After("execution(* it.pagopa.interop.probing.caller.consumer.PollingReceiver.*(..))")
  public void afterListener(JoinPoint joinPoint) {
    AWSXRay.endSegment();
  }
}
