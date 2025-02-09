package com.indifier.controller;

import com.indifier.service.TriggerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping(path = "/trigger/v1")
public class TriggerController {

  private final TriggerService triggerService;

  public TriggerController(TriggerService triggerService) {
    this.triggerService = triggerService;
  }

  @PostMapping(path = "/rsi")
  public void triggerRsiCheck() throws Exception {
    try {
      triggerService.triggerRsiCheck();
      logger.info("Rsi check successful");
    } catch (Exception ex) {
      logger.error("Error triggering RSI check due to ", ex);
      throw ex;
    }
  }

  private static final Logger logger = LoggerFactory.getLogger(TriggerController.class);
}
