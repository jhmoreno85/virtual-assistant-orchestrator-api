package com.va.orchestrator.api.service.eventflow;

import com.ibm.watson.assistant.v1.model.MessageResponse;

import com.va.orchestrator.api.common.Constants;
import com.va.orchestrator.api.exception.ApplicationException;
import com.va.orchestrator.api.exception.BadRequestException;
import com.va.orchestrator.api.exception.ErrorCode;
import com.va.orchestrator.api.model.sendmessage.Context;
import com.va.orchestrator.api.model.sendmessage.Input;
import com.va.orchestrator.api.model.sendmessage.SendMessageRequest;
import com.va.orchestrator.api.model.sendmessage.SendMessageResponse;
import com.va.orchestrator.api.service.customer.factory.OperationFactory;
import com.va.orchestrator.api.service.customer.factory.OperationType;
import com.va.orchestrator.api.service.dao.ConversationalDatabaseService;
import com.va.orchestrator.api.service.piiscrubber.PiiScrubberService;
import com.va.orchestrator.api.service.wdc.assistant.WatsonAssistantService;
import com.va.orchestrator.api.util.CallCounterStrategy;

import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static com.va.orchestrator.api.common.Constants.MAX_NUMBER_OF_CALLS;
import static com.va.orchestrator.api.util.ContextUtil.getOperation;
import static com.va.orchestrator.api.util.ContextUtil.populateCommunicationContext;
import static com.va.orchestrator.api.util.EventFlowUtil.createSendMessageResponse;

/**
 * @author huerta.jorge at gmail.com
 */
@Slf4j
@Service
public class EventFlowServiceImpl implements EventFlowService {

  private final PiiScrubberService piiScrubberService;
  private final ConversationalDatabaseService conversationalDatabaseService;
  private final WatsonAssistantService watsonAssistantService;
  private final OperationFactory operationFactory;

  public EventFlowServiceImpl(
      PiiScrubberService piiScrubberService,
      ConversationalDatabaseService conversationalDatabaseService,
      WatsonAssistantService watsonAssistantService,
      OperationFactory operationFactory) {
    this.piiScrubberService = piiScrubberService;
    this.conversationalDatabaseService = conversationalDatabaseService;
    this.watsonAssistantService = watsonAssistantService;
    this.operationFactory = operationFactory;
  }

  @Override
  public SendMessageResponse sendMessage(SendMessageRequest sendMessageRequest)
      throws BadRequestException, ApplicationException {
    String inputText =
        Optional.ofNullable(sendMessageRequest)
            .map(SendMessageRequest::getInput)
            .map(Input::getText)
            .map(String::trim)
            .filter(text -> !text.isEmpty())
            .orElseThrow(
                () ->
                    new BadRequestException(
                        "Input text is null or empty", new Exception(), ErrorCode.INVALID_PAYLOAD));

    String conversationId =
        Optional.of(sendMessageRequest)
            .map(SendMessageRequest::getContext)
            .map(Context::getConversationId)
            .map(String::trim)
            .filter(currConversationId -> !currConversationId.isEmpty())
            .orElse(Constants.EMPTY);

    Map<String, Object> context =
        conversationId.isEmpty()
            ? new HashMap<>()
            : conversationalDatabaseService.findContextByConversationId(conversationId);
    populateCommunicationContext(context, sendMessageRequest);

    String inputTextWithoutPii = piiScrubberService.sanitizeInputText(inputText);

    MessageResponse messageResponse;
    Optional<String> operationField;
    CallCounterStrategy callCounterStrategy = new CallCounterStrategy(MAX_NUMBER_OF_CALLS);
    do {
      messageResponse =
          watsonAssistantService.message(context, conversationId, inputTextWithoutPii);
      conversationalDatabaseService.saveOrUpdateContext(messageResponse);
      operationField = getOperation(messageResponse.getContext().getProperties());
      if (operationField.isPresent()) {
        context =
            operationFactory
                .getOperation(OperationType.getOperationType(operationField.get()))
                .fireOperation(messageResponse.getContext().getProperties());
      }
    } while (operationField.isPresent() && callCounterStrategy.shouldCall());

    messageResponse.getInput().setText(inputText);
    conversationalDatabaseService.saveUtterance(messageResponse);
    return createSendMessageResponse(messageResponse, inputText);
  }
}
