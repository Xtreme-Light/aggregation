package com.light.spring.message.integration.activator;

import com.light.spring.message.model.Cargo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.integration.annotation.MessageEndpoint;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.handler.annotation.Header;

@MessageEndpoint
@Slf4j
public class DiscardedCargoMessageServiceActivator {

  @ServiceActivator
  public void handleDiscardedCargo(Cargo cargo, @Header("CARGO_BATCH_ID") long batchId) {
    log.info("Message in Batch[" + batchId + "] is received with Discarded payload : " + cargo);
  }
}
