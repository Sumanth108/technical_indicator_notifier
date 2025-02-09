package com.indifier.api;

import com.indifier.domain.HistoricalResponse;
import com.indifier.domain.OhlcCandle;
import com.indifier.domain.StockData;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class UpstoxApi {

  @Autowired
  @Qualifier("upstox")
  private WebClient.Builder webClient;

  public List<OhlcCandle> getStockData(
      String instrumentKey, String timeframe, String toDate, String fromDate) throws Exception {
    try {
      Mono<HistoricalResponse> responseMono =
          webClient
              .build()
              .get()
              .uri(
                  "/{instrumentKey}/{timeframe}/{toDate}/{fromDate}",
                  instrumentKey,
                  timeframe,
                  toDate,
                  fromDate)
              .retrieve()
              .bodyToMono(HistoricalResponse.class);
      HistoricalResponse response = responseMono.block();
      if (response == null) {
        throw new Exception("Null response from upstox api");
      }
      return getOhlcCandles(response.data);
    } catch (Exception ex) {
      logger.error("Error calling upstoxapi", ex);
      throw ex;
    }
  }

  private List<OhlcCandle> getOhlcCandles(StockData data) {
    ArrayList<OhlcCandle> candles = new ArrayList<>();

    for (List<Object> candle : data.candles) {
      candles.add(new OhlcCandle((String) candle.get(0), (Number) candle.get(4)));
    }
    return candles;
  }

  private static final Logger logger = LoggerFactory.getLogger(UpstoxApi.class);
}
