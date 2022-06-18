package com.light.spring.message;

import com.light.spring.message.integration.gateway.ICargoGateway;
import com.light.spring.message.integration.gateway.ICargoSingleGateway;
import com.light.spring.message.model.Cargo;
import com.light.spring.message.model.Cargo.ShippingType;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.MessageChannel;

@SpringBootApplication
@Slf4j
public class SpringCloudMessageApplication {

  public static void main(String[] args) {
    final ConfigurableApplicationContext run = SpringApplication.run(
        SpringCloudMessageApplication.class, args);

    // 以网关为入口 ，使用集成流 处理数据
    final ICargoGateway iCargoGateway = run.getBean(ICargoGateway.class);
    getCargoBatchMap()
        .forEach(
            (batchId, cargoList) ->
                iCargoGateway.processCargoRequest(
                    MessageBuilder.withPayload(cargoList)
                        .setHeader("CARGO_BATCH_ID", batchId)
                        .build()));
    final ICargoSingleGateway iCargoSingleGateway = run.getBean(ICargoSingleGateway.class);
    // 这里通过网关 发送数据至MQ，而另一边通过声明的通道配置，使用IntegrationFlow直接处理数据
    iCargoSingleGateway.supplier(
        new Cargo(1, "AAAAAAAAAAAAAAAA", "Address1", 0.5, ShippingType.DOMESTIC, 1,
            1, ""));

    iCargoSingleGateway.supplier(
        new Cargo(2, "BBBBBBBBBBBBBBBB", "Address1", 0.5, ShippingType.DOMESTIC, 1,
            1, ""));

    iCargoSingleGateway.supplier(
        new Cargo(3, "CCCCCCCCCCCCCCC", "Address1", 0.5, ShippingType.DOMESTIC, 1,
            1, ""));

    MessageChannel bean = run.getBean("amqpMessage-in-0", MessageChannel.class);
    log.info("bean" + bean.getClass());
    bean = run.getBean("AMQP_MESSAGE_INPUT", MessageChannel.class);
    log.info("bean" + bean.getClass());


  }


  @Bean
  public Consumer<String> temp() {
    return v -> log.info("============{}", v);
  }

  private static Map<Integer, List<Cargo>> getCargoBatchMap() {
    Map<Integer, List<Cargo>> cargoBatchMap = new HashMap<>();

    cargoBatchMap.put(
        1,
        Arrays.asList(
            new Cargo(1, "Receiver_Name1", "Address1", 0.5, ShippingType.DOMESTIC, 1,
                1, ""),
            // Second cargo is filtered due to weight limit
            new Cargo(
                2, "Receiver_Name2", "Address2", 2_000, ShippingType.INTERNATIONAL, 1,
                1, ""),
            new Cargo(3, "Receiver_Name3", "Address3", 5, ShippingType.INTERNATIONAL, 1,
                1, ""),
            // Fourth cargo is not processed due to no shipping type found
            new Cargo(4, "Receiver_Name4", "Address4", 8, null, 1,
                1, "")));

    cargoBatchMap.put(
        2,
        Arrays.asList(
            // Fifth cargo is filtered due to weight limit
            new Cargo(5, "Receiver_Name5", "Address5", 1_200, ShippingType.INTERNATIONAL, 1,
                1, ""),
            new Cargo(6, "Receiver_Name6", "Address6", 20, ShippingType.INTERNATIONAL, 1,
                1, ""),
            // Seventh cargo is not processed due to no shipping type found
            new Cargo(7, "Receiver_Name7", "Address7", 5, ShippingType.INTERNATIONAL, 1,
                1, "")));

    cargoBatchMap.put(
        3,
        Arrays.asList(
            new Cargo(8, "Receiver_Name8", "Address8",
                4.75, ShippingType.INTERNATIONAL, 1,
                1, ""),
            new Cargo(
                9, "Receiver_Name9", "Address9", 4.75, ShippingType.INTERNATIONAL, 1, 1,
                "1")));

    return Collections.unmodifiableMap(cargoBatchMap);
  }

}
