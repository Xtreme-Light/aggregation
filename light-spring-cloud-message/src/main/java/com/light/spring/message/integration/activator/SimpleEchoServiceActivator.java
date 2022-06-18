package com.light.spring.message.integration.activator;

import com.light.spring.message.model.Cargo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.integration.annotation.MessageEndpoint;
import org.springframework.integration.annotation.ServiceActivator;

@MessageEndpoint
@Slf4j
public class SimpleEchoServiceActivator {

  @ServiceActivator
  public void handle(Cargo messages) {
    log.info("打印数据{}", messages);
  }
}
