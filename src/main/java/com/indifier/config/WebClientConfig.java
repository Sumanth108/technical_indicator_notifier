package com.indifier.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

  @Bean(name = "upstox")
  public WebClient.Builder webclientBuilder() {
    return WebClient.builder().baseUrl("https://api-v2.upstox.com/v2/historical-candle");
  }

  @Bean(name = "telegram")
  public WebClient.Builder telegramWebclientBuilder() {
    return WebClient.builder().baseUrl("https://api.telegram.org");
  }
}
