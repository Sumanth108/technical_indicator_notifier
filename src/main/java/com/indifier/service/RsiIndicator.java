package com.indifier.service;

import com.indifier.api.UpstoxApi;
import com.indifier.domain.OhlcCandle;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RsiIndicator {

  @Autowired private UpstoxApi upstoxApi;

  public Map<String, Number> getRsiSetupStocks(Set<String> stockCodes, Integer rsi)
      throws Exception {
    Map<String, Number> eligibleStocksMap = new HashMap<>();

    for (String stockSymbol : stockCodes) {
      addRsiEligibleStocks(rsi, stockSymbol, eligibleStocksMap, "day", 3);
      addRsiEligibleStocks(rsi, stockSymbol, eligibleStocksMap, "week", 12);
    }

    return eligibleStocksMap;
  }

  private void addRsiEligibleStocks(
      Integer rsi,
      String stockSymbol,
      Map<String, Number> eligibleStocksMap,
      String timeframe,
      Integer monthsToConsider)
      throws Exception {
    List<OhlcCandle> stockData =
        getStockData(
            stockSymbol, timeframe, getCurrentDate(), getCurrentDateMinusMonths(monthsToConsider));
    List<Double> rsiValue = getRsi(stockData);
    if (rsiValue.get(rsiValue.size() - 1) <= rsi) {
      eligibleStocksMap.put(stockSymbol, rsiValue.get(rsiValue.size() - 1));
      logger.info(
          "Stock - {} eligible due to rsi - {}", stockSymbol, rsiValue.get(rsiValue.size() - 1));
    }
  }

  public static double pineSMA(List<Double> data, int length, int endIndex) {
    if (endIndex < length - 1) return Double.NaN;

    double sum = 0.0;
    for (int i = 0; i < length; i++) {
      sum += data.get(endIndex - i) / length;
    }
    return sum;
  }

  public static List<Double> pineRMA(List<Double> data, int length) {
    List<Double> rmaValues = new ArrayList<>();
    double alpha = 1.0 / length;
    double sum = 0.0;

    for (int i = 0; i < data.size(); i++) {
      if (i < length - 1) {
        rmaValues.add(Double.NaN);
      } else if (i == length - 1) {
        sum = pineSMA(data, length, i);
        rmaValues.add(sum);
      } else {
        sum = alpha * data.get(i) + (1 - alpha) * sum;
        rmaValues.add(sum);
      }
    }
    return rmaValues;
  }

  public static List<Double> getRsi(List<OhlcCandle> stockData) {
    int period = 14;

    Collections.reverse(stockData);
    List<Double> rsiValues = new ArrayList<>();

    List<Double> gains = new ArrayList<>();
    List<Double> losses = new ArrayList<>();

    for (int i = 0; i < stockData.size(); i++) {
      if (i == 0) {
        gains.add(0.0);
        losses.add(0.0);
        continue;
      }

      double change =
          stockData.get(i).getClose().doubleValue() - stockData.get(i - 1).getClose().doubleValue();
      gains.add(Math.max(change, 0));
      losses.add(Math.max(-change, 0));
    }

    List<Double> avgGain = pineRMA(gains, period);
    List<Double> avgLoss = pineRMA(losses, period);

    for (int i = 0; i < stockData.size(); i++) {
      if (i < period) {
        rsiValues.add(Double.NaN);
        continue;
      }

      double rs = (avgLoss.get(i) == 0) ? 100 : avgGain.get(i) / avgLoss.get(i);
      double rsi = 100 - 100 / (1 + rs);
      rsiValues.add(rsi);
    }

    return rsiValues;
  }

  private List<OhlcCandle> getStockData(
      String code, String timeframe, String toDate, String fromDate) throws Exception {
    List<OhlcCandle> stockData =
        upstoxApi.getStockData("NSE_EQ|" + code, timeframe, toDate, fromDate);
    logger.info("Received upstoxapi data for - {}", code);

    // upstox rate limiting
    Thread.sleep(125);
    return stockData;
  }

  private String getCurrentDate() {
    LocalDate currentDate = LocalDate.now();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    return currentDate.format(formatter);
  }

  private String getCurrentDateMinusMonths(Integer months) {
    LocalDate currentDate = LocalDate.now();
    LocalDate dateMinusOneYear = currentDate.minusMonths(months);
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    return dateMinusOneYear.format(formatter);
  }

  private static final Logger logger = LoggerFactory.getLogger(RsiIndicator.class);
}
