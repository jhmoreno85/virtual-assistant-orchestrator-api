package com.va.orchestrator.api.util;

import com.va.orchestrator.api.exception.ErrorCode;
import com.va.orchestrator.api.exception.RetryAttemptsReachedException;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/** @author huerta.jorge at gmail.com */
@Slf4j
public class RetryStrategy {

  private int numberOfRetries;
  private final long delayToRetry;
  private final ErrorCode errorCode;

  public RetryStrategy(int numberOfRetries, long delayToRetry, ErrorCode errorCode) {
    this.numberOfRetries = numberOfRetries;
    this.delayToRetry = delayToRetry;
    this.errorCode = errorCode;
  }

  public boolean shouldRetry() {
    return 0 < this.numberOfRetries;
  }

  public void errorOccurred(String message) throws RetryAttemptsReachedException {
    this.numberOfRetries--;
    if (!shouldRetry()) {
      throw new RetryAttemptsReachedException(message, new Exception(), errorCode);
    } else {
      try {
        TimeUnit.MILLISECONDS.sleep(this.delayToRetry);
      } catch (InterruptedException e) {
        log.error("An error has occurred: ", e);
        Thread.currentThread().interrupt();
      }
    }
  }
}
