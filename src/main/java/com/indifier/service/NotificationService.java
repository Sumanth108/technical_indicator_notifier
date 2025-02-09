package com.indifier.service;

import com.indifier.api.TelegramApi;
import com.indifier.domain.Stock;
import java.util.Set;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

  private final TelegramApi telegramApi;

  public NotificationService(TelegramApi telegramApi) {
    this.telegramApi = telegramApi;
  }

  public void notifyTelegram(Set<Stock> stocks) {

    String message =
        stocks.stream()
            .map(Stock::toString)
            .reduce((line1, line2) -> line1 + "\n\n" + line2)
            .orElse("");

    telegramApi.sendMessage(message);
  }
}
