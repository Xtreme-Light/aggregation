package com.light.spring.message.integration.transformer;


import com.light.spring.message.model.Cargo;
import com.light.spring.message.model.DomesticCargoMessage;
import com.light.spring.message.model.DomesticCargoMessage.Region;
import com.light.spring.message.model.InternationalCargoMessage;
import com.light.spring.message.model.InternationalCargoMessage.DeliveryOption;
import org.springframework.integration.annotation.MessageEndpoint;
import org.springframework.integration.annotation.Transformer;

@MessageEndpoint
public class CargoTransformer {

  @Transformer
  public DomesticCargoMessage transformDomesticCargo(Cargo cargo) {
    return new DomesticCargoMessage(cargo, Region.fromValue(cargo.getRegion()));
  }

  @Transformer
  public InternationalCargoMessage transformInternationalCargo(Cargo cargo) {
    return new InternationalCargoMessage(cargo,
        getDeliveryOption(cargo.getDeliveryDayCommitment()));
  }

  private DeliveryOption getDeliveryOption(int deliveryDayCommitment) {
    if (deliveryDayCommitment == 1) {
      return DeliveryOption.NEXT_FLIGHT;
    } else if (deliveryDayCommitment == 2) {
      return DeliveryOption.PRIORITY;
    } else if (deliveryDayCommitment > 2 && deliveryDayCommitment < 5) {
      return DeliveryOption.ECONOMY;
    } else {
      return DeliveryOption.STANDART;
    }
  }

}
