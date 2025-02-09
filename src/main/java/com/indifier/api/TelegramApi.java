package com.indifier.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class TelegramApi {

  private static final String TELEGRAM_BOT_TOKEN = "test";
  private static final String TELEGRAM_CHAT_ID = "testt";

  @Autowired
  @Qualifier("telegram")
  private WebClient.Builder webClient;

  public void sendMessage(String message) {
    String endpoint = String.format("/bot%s/sendMessage", TELEGRAM_BOT_TOKEN);
    try {
      webClient
          .build()
          .post()
          .uri(
              uriBuilder ->
                  uriBuilder
                      .path(endpoint)
                      .queryParam("chat_id", TELEGRAM_CHAT_ID)
                      .queryParam("text", message)
                      .build())
          .retrieve()
          .bodyToMono(String.class)
          .block();
    } catch (Exception ex) {
      logger.error("Error calling telegram api", ex);
      throw ex;
    }
  }

  private static final Logger logger = LoggerFactory.getLogger(TelegramApi.class);
}
