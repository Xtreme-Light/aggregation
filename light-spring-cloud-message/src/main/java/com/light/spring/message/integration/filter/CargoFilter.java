package com.light.spring.message.integration.filter;


import com.light.spring.message.model.Cargo;
import org.springframework.integration.annotation.Filter;
import org.springframework.integration.annotation.MessageEndpoint;

@MessageEndpoint
public class CargoFilter {

  private static final long CARGO_WEIGHT_LIMIT = 1_000;

  /**
   * Checks weight of cargo and filters if it exceeds limit.
   *
   * @param cargo message
   * @return check result
   */
  @Filter
  public boolean filterIfCargoWeightExceedsLimit(Cargo cargo) {
    return cargo.getWeight() <= CARGO_WEIGHT_LIMIT;
  }
}
