package com.light.spring.message.model;


public class InternationalCargoMessage extends CargoMessage {

  public enum DeliveryOption {
    NEXT_FLIGHT, PRIORITY, ECONOMY, STANDART
  }

  private final DeliveryOption deliveryOption;

  public InternationalCargoMessage(Cargo cargo, DeliveryOption deliveryOption) {
    super(cargo);
    this.deliveryOption = deliveryOption;
  }

  public DeliveryOption getDeliveryOption() {
    return deliveryOption;
  }

  @Override
  public String toString() {
    return "InternationalCargoMessage [cargo=" + super.toString() + ", deliveryOption="
        + deliveryOption + "]";
  }

}
