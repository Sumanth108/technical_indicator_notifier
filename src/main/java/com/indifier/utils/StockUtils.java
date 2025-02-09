package com.indifier.utils;

import com.indifier.domain.Stock;
import com.indifier.domain.StockSymbol;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.springframework.stereotype.Component;

@Component
public class StockUtils {

  public Set<Stock> getStockData(
      Map<String, Number> stockSymbols, Map<String, StockSymbol> nifty100) {
    Set<Stock> stocks = new HashSet<>();

    stockSymbols.forEach(
        (code, indicatorValue) ->
            stocks.add(getStock(nifty100.get(code), indicatorValue, "Nifty100")));
    return stocks;
  }

  public Stock getStock(StockSymbol stockSymbol, Number indicatorValue, String universe) {
    return Stock.builder()
        .name(stockSymbol.getName())
        .symbol(stockSymbol.getSymbol())
        .indicatorValue(indicatorValue)
        .universe(universe)
        .build();
  }
}
