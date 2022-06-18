package com.light.spring.message.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Cargo {

  public enum ShippingType {
    DOMESTIC,
    INTERNATIONAL
  }

  private long trackingId;
  private String receiverName;
  private String deliveryAddress;
  private double weight;
  private ShippingType shippingType;
  private int deliveryDayCommitment;
  private int region;
  private String description;

}
