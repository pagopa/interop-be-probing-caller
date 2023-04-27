package it.pagopa.interop.probing.caller;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class InteropCallerApplication {

  public static void main(String[] args) {
    SpringApplication.run(InteropCallerApplication.class, args);
  }

}
