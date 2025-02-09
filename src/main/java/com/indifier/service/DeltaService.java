package com.indifier.service;

import com.indifier.domain.DeltaStocks;
import com.indifier.utils.StockUtils;
import java.util.Set;
import org.springframework.stereotype.Service;

@Service
public class DeltaService {
  private StockUtils stockUtils;

  public DeltaService(StockUtils stockUtils) {
    this.stockUtils = stockUtils;
  }

  public DeltaStocks getDeltaStocks(Set<String> stocks) {
    return new DeltaStocks();
  }
}
