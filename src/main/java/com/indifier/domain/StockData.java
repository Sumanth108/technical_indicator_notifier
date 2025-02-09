package com.indifier.domain;

import java.util.List;
import lombok.Data;

@Data
public class StockData {
  public List<List<Object>> candles;
}
