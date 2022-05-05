package com.va.orchestrator.api.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

/** @author huerta.jorge at gmail.com */
public enum ErrorCode {
  INVALID_PAYLOAD(100, HttpStatus.BAD_REQUEST.value(), "Invalid payload"),
  JSON_CONVERSION_ERROR(101, HttpStatus.INTERNAL_SERVER_ERROR.value(), "Exception during JSON String conversion"),
  WATSON_ASSISTANT_ERROR(110, HttpStatus.INTERNAL_SERVER_ERROR.value(), "Watson Assistant error"),
  PII_SCRUBBER_ERROR(110, HttpStatus.INTERNAL_SERVER_ERROR.value(), "Pii Scrubber error"),
  COMMAND_NOT_FOUND_ERROR(120, HttpStatus.INTERNAL_SERVER_ERROR.value(), "Command not found"),
  UNDEFINED_ERROR(-1, HttpStatus.INTERNAL_SERVER_ERROR.value(), "Undefined error");

  @Getter private final int code;
  @Getter private final int httpStatus;
  @Getter private final String description;

  ErrorCode(int code, int httpStatus, String description) {
    this.code = code;
    this.httpStatus = httpStatus;
    this.description = description;
  }
}
