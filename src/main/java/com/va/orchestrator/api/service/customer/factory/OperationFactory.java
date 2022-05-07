package com.va.orchestrator.api.service.customer.factory;

import com.va.orchestrator.api.exception.ApplicationException;
import com.va.orchestrator.api.exception.ErrorCode;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.EnumMap;
import java.util.Map;
import java.util.Set;

/**
 * @author huerta.jorge at gmail.com
 */
@Component
public class OperationFactory {

  private Map<OperationType, Operation> operations;

  @Autowired
  public OperationFactory(Set<Operation> operationSet) {
    registerOperations(operationSet);
  }

  private void registerOperations(Set<Operation> operationSet) {
    operations = new EnumMap<>(OperationType.class);
    operationSet.forEach(operation -> operations.put(operation.getOperationType(), operation));
  }

  public Operation getOperation(OperationType operationType) throws ApplicationException {
    Operation operation = operations.get(operationType);
    if (null == operation) {
      throw new ApplicationException(
          "Operation not defined", new Exception(), ErrorCode.COMMAND_NOT_FOUND_ERROR);
    }
    return operation;
  }
}
