package com.va.orchestrator.api.service.dao;

import com.ibm.watson.assistant.v1.model.MessageResponse;

import com.va.orchestrator.api.exception.BadRequestException;
import com.va.orchestrator.api.exception.ErrorCode;
import com.va.orchestrator.api.model.dao.UserConversation;
import com.va.orchestrator.api.model.dao.UserConversationDetailed;
import com.va.orchestrator.api.repository.UserConversationDetailedRepository;
import com.va.orchestrator.api.repository.UserConversationRepository;

import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.va.orchestrator.api.common.Constants.JOINING_DELIMITER;
import static com.va.orchestrator.api.common.Constants.LIMIT_ENTITIES;
import static com.va.orchestrator.api.common.Constants.LIMIT_INTENTS;
import static com.va.orchestrator.api.util.ContextUtil.jsonStringToMap;
import static com.va.orchestrator.api.util.ContextUtil.mapToJsonString;

/**
 * @author huerta.jorge at gmail.com
 */
@Slf4j
@Service
public class ConversationalDatabaseServiceImpl implements ConversationalDatabaseService {

  private static final String DEFAULT_FEEDBACK_VALUE = "";

  private final UserConversationRepository userConversationRepository;
  private final UserConversationDetailedRepository userConversationDetailedRepository;

  public ConversationalDatabaseServiceImpl(
      UserConversationRepository userConversationRepository,
      UserConversationDetailedRepository userConversationDetailedRepository) {
    this.userConversationRepository = userConversationRepository;
    this.userConversationDetailedRepository = userConversationDetailedRepository;
  }

  @Override
  public void saveOrUpdateContext(MessageResponse messageResponse) {
    log.info(
        "Saving conversation context, conversationId={}",
        messageResponse.getContext().getConversationId());
    Optional<UserConversation> userConversationOptional =
        userConversationRepository.findById(messageResponse.getContext().getConversationId());
    UserConversation userConversation;
    if (userConversationOptional.isPresent()) {
      userConversation = userConversationOptional.get();
      userConversation.setContext(mapToJsonString(messageResponse.getContext().getProperties()));
    } else {
      userConversation = new UserConversation();
      userConversation.setConversationId(messageResponse.getContext().getConversationId());
      userConversation.setContext(mapToJsonString(messageResponse.getContext().getProperties()));
      userConversation.setStartTs(new Date());
    }
    long start = System.currentTimeMillis();
    userConversationRepository.save(userConversation);
    long elapsedTime = System.currentTimeMillis() - start;
    log.info("Conversation context saved/updated successful, {} ms", elapsedTime);
  }

  @Override
  public Map<String, Object> findContextByConversationId(String conversationId)
      throws BadRequestException {
    log.info("Getting conversation context, conversationId={}", conversationId);
    long start = System.currentTimeMillis();
    Optional<UserConversation> userConversationOptional =
        userConversationRepository.findById(conversationId);
    long elapsedTime = System.currentTimeMillis() - start;
    if (userConversationOptional.isPresent()) {
      log.info("Conversation context retrieved successful, {} ms", elapsedTime);
      return jsonStringToMap(userConversationOptional.get().getContext());
    } else {
      throw new BadRequestException(
          "ConversationId not found", new Exception(), ErrorCode.INVALID_PAYLOAD);
    }
  }

  @Override
  public Long saveUtterance(MessageResponse messageResponse) {
    log.info(
        "Saving conversation utterance, conversationId={}",
        messageResponse.getContext().getConversationId());
    UserConversationDetailed userConversationDetailed = new UserConversationDetailed();
    userConversationDetailed.setConversationId(messageResponse.getContext().getConversationId());
    userConversationDetailed.setInputText(messageResponse.getInput().getText());
    userConversationDetailed.setOutputText(
        messageResponse.getOutput().getText().stream()
            .map(String::valueOf)
            .collect(Collectors.joining(JOINING_DELIMITER)));
    userConversationDetailed.setConfidence(messageResponse.getIntents().get(0).confidence());
    userConversationDetailed.setIntents(
        messageResponse.getIntents().stream()
            .limit(LIMIT_INTENTS)
            .map(runtimeIntent -> runtimeIntent.intent() + ":" + runtimeIntent.confidence())
            .collect(Collectors.joining(JOINING_DELIMITER)));
    userConversationDetailed.setEntities(
        messageResponse.getEntities().stream()
            .limit(LIMIT_ENTITIES)
            .map(runtimeEntity -> runtimeEntity.entity() + ":" + runtimeEntity.confidence())
            .collect(Collectors.joining(JOINING_DELIMITER)));
    userConversationDetailed.setTransTs(new Date());
    userConversationDetailed.setFeedback(DEFAULT_FEEDBACK_VALUE);
    long start = System.currentTimeMillis();
    Long rowId = userConversationDetailedRepository.save(userConversationDetailed).getId();
    long elapsedTime = System.currentTimeMillis() - start;
    log.info("Conversation utterance saved successful, {} ms", elapsedTime);
    return rowId;
  }

  @Override
  public void saveFeedback(Long id, String conversationId, String feedback) {
    log.info("Saving feedback, conversationId={}, rowId={}", conversationId, id);
    userConversationDetailedRepository
        .findById(id)
        .ifPresent(
            userConversationDetailed -> {
              if (null != conversationId
                  && conversationId.equals(userConversationDetailed.getConversationId())) {
                userConversationDetailed.setFeedback(feedback);
                long start = System.currentTimeMillis();
                userConversationDetailedRepository.save(userConversationDetailed);
                long elapsedTime = System.currentTimeMillis() - start;
                log.info("Feedback saved successful, {} ms", elapsedTime);
              }
            });
  }
}
