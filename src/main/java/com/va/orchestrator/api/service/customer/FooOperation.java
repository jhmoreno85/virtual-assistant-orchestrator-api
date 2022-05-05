package com.va.orchestrator.api.service.customer;

import com.va.orchestrator.api.service.customer.factory.Operation;
import com.va.orchestrator.api.service.customer.factory.OperationType;

import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Component;

import java.util.Map;

/** @author huerta.jorge at gmail.com */
@Slf4j
@Component
public class FooOperation implements Operation {

  @Override
  public Map<String, Object> fireOperation(Map<String, Object> map) {
    log.info("Executing {}, fireOperation method", this.getClass().getSimpleName());
    return map;
  }

  @Override
  public OperationType getOperationType() {
    return OperationType.FOO_OPERATION;
  }
}
