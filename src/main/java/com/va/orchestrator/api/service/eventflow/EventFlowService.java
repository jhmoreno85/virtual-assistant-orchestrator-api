package com.va.orchestrator.api.service.eventflow;

import com.va.orchestrator.api.exception.ApplicationException;
import com.va.orchestrator.api.exception.BadRequestException;
import com.va.orchestrator.api.model.sendmessage.SendMessageRequest;
import com.va.orchestrator.api.model.sendmessage.SendMessageResponse;

/** @author huerta.jorge at gmail.com */
public interface EventFlowService {
  SendMessageResponse sendMessage(SendMessageRequest payload)
      throws BadRequestException, ApplicationException;
}
