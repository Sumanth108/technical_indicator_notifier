package com.indifier.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class HistoricalResponse {

  public String status;
  public StockData data;
}
