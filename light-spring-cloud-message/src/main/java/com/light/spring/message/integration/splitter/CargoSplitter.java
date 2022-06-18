package com.light.spring.message.integration.splitter;


import com.light.spring.message.model.Cargo;
import java.util.List;
import org.springframework.integration.annotation.MessageEndpoint;
import org.springframework.integration.annotation.Splitter;
import org.springframework.messaging.Message;

@MessageEndpoint
public class CargoSplitter {

  @Splitter
  public List<Cargo> splitCargoList(Message<List<Cargo>> message) {
    return message.getPayload();
  }
}
