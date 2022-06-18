package com.light.spring.message.integration.gateway;


import com.light.spring.message.model.Cargo;
import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;

@MessagingGateway
public interface ICargoSingleGateway {

  @Gateway(requestChannel = "AMQP_MESSAGE_OUTPUT")
  void supplier(Cargo messages);

}
