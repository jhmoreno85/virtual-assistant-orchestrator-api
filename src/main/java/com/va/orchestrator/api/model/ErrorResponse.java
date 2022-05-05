package com.va.orchestrator.api.model;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/** @author huerta.jorge at gmail.com */
@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
public class ErrorResponse {
  private int code;
  private String error;
}
