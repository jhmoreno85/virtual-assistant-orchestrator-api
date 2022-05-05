package com.va.orchestrator.api.service.customer.factory;

import java.util.Map;

/** @author huerta.jorge at gmail.com */
public interface Operation {
  Map<String, Object> fireOperation(Map<String, Object> map);

  OperationType getOperationType();
}
