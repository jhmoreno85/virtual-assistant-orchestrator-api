package com.va.orchestrator.api.exception;

import lombok.Getter;

/** @author huerta.jorge at gmail.com */
public class BadRequestException extends Exception {

  private static final long serialVersionUID = 1L;

  @Getter private final ErrorCode errorCode;

  public BadRequestException(String message, Throwable cause, ErrorCode errorCode) {
    super(message, cause);
    this.errorCode = errorCode;
  }
}
