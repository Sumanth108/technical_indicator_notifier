package com.indifier.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
@AllArgsConstructor
public class OhlcCandle {

  private String timestamp;
  private Number close;
}
