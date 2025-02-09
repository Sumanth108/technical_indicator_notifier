package com.indifier.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.indifier.domain.StockSymbol;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class NiftyUtils {

  ObjectMapper objectMapper = new ObjectMapper();

  public Map<String, StockSymbol> getNifty100Stocks() throws IOException {
    Set<StockSymbol> stocks =
        objectMapper.readValue(
            new File("src/main/resources/json/nifty100.json"),
            objectMapper.getTypeFactory().constructCollectionType(Set.class, StockSymbol.class));

    return stocks.stream().collect(Collectors.toMap(StockSymbol::getIsinCode, obj -> obj));
  }

  public Map<String, StockSymbol> getNiftyMid150Stocks() throws IOException {
    Set<StockSymbol> stocks =
        objectMapper.readValue(
            new File("src/main/resources/json/niftymid150.json"),
            objectMapper.getTypeFactory().constructCollectionType(Set.class, StockSymbol.class));

    return stocks.stream().collect(Collectors.toMap(StockSymbol::getIsinCode, obj -> obj));
  }

  //  public Map<String, StockSymbol> getNifty100to200Stocks() throws IOException {
  //    Map<String, StockSymbol> nifty100 = getNifty100Stocks();
  //    Map<String, StockSymbol> nifty200 = getNifty200Stocks();
  //
  //    nifty200.values().removeIf(nifty100::containsValue);
  //    return nifty200;
  //  }
}
