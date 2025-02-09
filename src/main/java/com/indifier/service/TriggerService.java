package com.indifier.service;

import com.indifier.domain.Stock;
import com.indifier.domain.StockSymbol;
import com.indifier.utils.NiftyUtils;
import com.indifier.utils.StockUtils;
import java.util.*;
import org.springframework.stereotype.Service;

@Service
public class TriggerService {

  private final RsiIndicator rsiIndicator;
  private final NotificationService notificationService;
  private final NiftyUtils niftyUtils;
  private final StockUtils stockUtils;

  public TriggerService(
      RsiIndicator rsiIndicator,
      NotificationService notificationService,
      NiftyUtils niftyUtils,
      StockUtils stockUtils) {
    this.rsiIndicator = rsiIndicator;
    this.notificationService = notificationService;
    this.niftyUtils = niftyUtils;
    this.stockUtils = stockUtils;
  }

  public void triggerRsiCheck() throws Exception {
    Map<String, StockSymbol> nifty100 = niftyUtils.getNifty100Stocks();
    Map<String, StockSymbol> niftyMid150 = niftyUtils.getNiftyMid150Stocks();

    Map<String, Number> nifty100Stocks = rsiIndicator.getRsiSetupStocks(nifty100.keySet(), 30);
    //    Map<String, Number> niftyMid150Stocks =
    //        rsiIndicator.getRsiSetupStocks(niftyMid150.keySet(), 20);
    //    niftyMid150Stocks.putAll(nifty100Stocks);
    niftyMid150.putAll(nifty100);
    Set<Stock> nifty250StockData = stockUtils.getStockData(nifty100Stocks, nifty100);
    notificationService.notifyTelegram(nifty250StockData);
  }
}
