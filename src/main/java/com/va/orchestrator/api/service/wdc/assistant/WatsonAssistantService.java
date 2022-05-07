package com.va.orchestrator.api.service.wdc.assistant;

import com.ibm.watson.assistant.v1.model.MessageResponse;

import com.va.orchestrator.api.exception.ApplicationException;

import java.util.Map;

/**
 * @author huerta.jorge at gmail.com
 */
public interface WatsonAssistantService {
  MessageResponse message(String conversationId, Map<String, Object> context, String text)
      throws ApplicationException;
}
