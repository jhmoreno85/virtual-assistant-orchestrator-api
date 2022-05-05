package com.va.orchestrator.api.service.customer.factory;

import java.util.Arrays;

/** @author huerta.jorge at gmail.com */
public enum OperationType {
  FOO_OPERATION("foo_operation"),
  BAR_OPERATION("bar_operation"),
  UNDEFINED("undefined");

  private final String operationName;

  OperationType(String operationName) {
    this.operationName = operationName;
  }

  public static OperationType getOperationType(String operationName) {
    return Arrays.stream(OperationType.values())
        .filter(operationType -> operationType.operationName.equals(operationName))
        .findAny()
        .orElse(UNDEFINED);
  }
}
