package com.va.orchestrator.api.exception;

import lombok.Getter;

/**
 * @author huerta.jorge at gmail.com
 */
@Getter
public class ApplicationException extends Exception {

  private static final long serialVersionUID = 1L;

  private final ErrorCode errorCode;

  public ApplicationException(String message, Throwable cause, ErrorCode errorCode) {
    super(message, cause);
    this.errorCode = errorCode;
  }
}
