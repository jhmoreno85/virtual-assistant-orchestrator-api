package com.va.orchestrator.api.service.dao;

import com.ibm.watson.assistant.v1.model.MessageResponse;
import com.va.orchestrator.api.exception.BadRequestException;

import java.util.Map;

/**
 * @author huerta.jorge at gmail.com
 */
public interface ConversationalDatabaseService {
  void upsertContext(MessageResponse messageResponse);

  Map<String, Object> findContextByConversationId(String conversationId) throws BadRequestException;

  Long saveUtterance(MessageResponse messageResponse);

  void saveFeedback(Long id, String conversationId, String feedback);
}
