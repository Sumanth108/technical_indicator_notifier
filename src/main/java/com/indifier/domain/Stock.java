package com.indifier.domain;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Getter
@Setter
public class Stock {

  private String name;
  private String symbol;
  private String universe;
  private Number indicatorValue;

  @Override
  public String toString() {
    return String.format("%s, %s, %s, %s", name, symbol, universe, indicatorValue);
  }
}
