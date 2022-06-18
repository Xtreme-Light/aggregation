package com.light.spring.message.integration;

import com.light.spring.message.integration.activator.CargoServiceActivator;
import com.light.spring.message.integration.activator.DiscardedCargoMessageServiceActivator;
import com.light.spring.message.integration.activator.SimpleEchoServiceActivator;
import com.light.spring.message.integration.splitter.CargoSplitter;
import com.light.spring.message.integration.transformer.CargoTransformer;
import com.light.spring.message.model.Cargo;
import com.light.spring.message.model.Cargo.ShippingType;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.MessageChannels;

@EnableIntegration
@Configuration
// 扫描集成组件
@IntegrationComponentScan("com.light.spring.message.integration")
public class IntegrationConfiguration {

  private static final long CARGO_WEIGHT_LIMIT = 1_000;

  /**
   * 通过 dsl 领域定义语言 构建一个集成流，该通道通过显式调用gateway 作为入口
   *
   * @param cargoSplitter                         分裂器
   * @param discardedCargoMessageServiceActivator 丢弃获取监听
   * @param cargoTransformer                      转换
   * @param cargoServiceActivator                 处理器
   */
  @Bean
  public IntegrationFlow cargoFlow(
      CargoSplitter cargoSplitter,
      DiscardedCargoMessageServiceActivator discardedCargoMessageServiceActivator,
      CargoTransformer cargoTransformer,
      CargoServiceActivator cargoServiceActivator
  ) {
    return IntegrationFlows
        .from("cargoGWDefaultRequestChannel")
        .split(cargoSplitter)
        .filter(
            (Cargo cargo) -> cargo.getWeight() <= CARGO_WEIGHT_LIMIT,
            e -> e.discardFlow(df ->
                df.channel(MessageChannels.queue())
                    .handle(discardedCargoMessageServiceActivator)
            )
        )
        .<Cargo, String>route(cargo ->
            {
              if (cargo.getShippingType() == ShippingType.DOMESTIC) {
                return "DOMESTIC";
              } else if (cargo.getShippingType() == ShippingType.INTERNATIONAL) {
                return "INTERNATIONAL";
              }
              return "nullChannel";
            },
            mapping -> mapping.subFlowMapping("DOMESTIC",
                sf -> sf
                    .transform(cargoTransformer, "transformDomesticCargo")
                    .bridge()
            ).subFlowMapping("INTERNATIONAL",
                sf -> sf
                    .transform(cargoTransformer, "transformInternationalCargo")
                    .bridge()
            )
        ).aggregate()
        .handle(cargoServiceActivator)
        .get();
  }

  // 不通过网关，使用配置的方式，声明一个通道，该通道配置了stream的destination 和 group，从该通道中直接获取队列消息，进行消费
  @Bean
  public IntegrationFlow directFlow(
      SimpleEchoServiceActivator simpleEchoServiceActivator
  ) {
    return IntegrationFlows
        .from("AMQP_MESSAGE_INPUT")
//        .from("AMQP_MESSAGE_INPUT")
        .handle(simpleEchoServiceActivator)
        .log().get();
  }

}
