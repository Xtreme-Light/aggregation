package com.light.spring.message.integration.gateway;


import com.light.spring.message.model.Cargo;
import java.util.List;
import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.messaging.Message;

@MessagingGateway(defaultRequestChannel = "cargoGWDefaultRequestChannel")
public interface ICargoGateway {

  @Gateway
  void processCargoRequest(Message<List<Cargo>> message);

}
