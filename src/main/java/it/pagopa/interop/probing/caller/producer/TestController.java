package it.pagopa.interop.probing.caller.producer;

import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import it.pagopa.interop.probing.caller.consumer.PollingReceiver;

@RestController
public class TestController {

  @Autowired
  PollingReceiver poll;

  @GetMapping("/")
  public void lello() throws IOException {
    poll.receiveStringMessage(null);
  }

}
