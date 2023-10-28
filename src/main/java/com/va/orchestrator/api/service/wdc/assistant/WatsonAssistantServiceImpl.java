package com.va.orchestrator.api.service.wdc.assistant;

import com.ibm.cloud.sdk.core.http.HttpConfigOptions;
import com.ibm.cloud.sdk.core.security.IamAuthenticator;
import com.ibm.watson.assistant.v1.Assistant;
import com.ibm.watson.assistant.v1.model.Context;
import com.ibm.watson.assistant.v1.model.MessageInput;
import com.ibm.watson.assistant.v1.model.MessageOptions;
import com.ibm.watson.assistant.v1.model.MessageResponse;

import com.va.orchestrator.api.exception.ApplicationException;
import com.va.orchestrator.api.exception.ErrorCode;
import com.va.orchestrator.api.exception.RetryAttemptsReachedException;
import com.va.orchestrator.api.util.RetryStrategy;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @author huerta.jorge at gmail.com
 */
@Slf4j
@Service
public class WatsonAssistantServiceImpl implements WatsonAssistantService {

  private static final String CONTENT_TYPE = "Content-Type";
  private static final String APPLICATION_JSON_CHARSET_UTF_8 = "application/json; charset=utf-8";

  private final Assistant assistant;
  private final String workspaceId;
  private final boolean alternateIntents;
  private final int retries;
  private final int delay;

  public WatsonAssistantServiceImpl(@Value("${wdc.assistant.endpoint}") String endpoint,
                                    @Value("${wdc.assistant.version}") String version,
                                    @Value("${wdc.assistant.api-key}") String apiKey,
                                    @Value("${wdc.assistant.workspace-id}") String workspaceId,
                                    @Value("${wdc.assistant.alternate-intents}") boolean alternateIntents,
                                    @Value("${wdc.assistant.retries}") int retries,
                                    @Value("${wdc.assistant.delay}") int delay) {
    IamAuthenticator authenticator = new IamAuthenticator(apiKey);
    this.assistant = new Assistant(version, authenticator);
    this.assistant.setServiceUrl(endpoint);
    HttpConfigOptions configOptions = new HttpConfigOptions.Builder()
            .disableSslVerification(true)
            .build();
    this.assistant.configureClient(configOptions);
    Map<String, String> headers = new HashMap<>();
    headers.put(CONTENT_TYPE, APPLICATION_JSON_CHARSET_UTF_8);
    this.assistant.setDefaultHeaders(headers);
    this.alternateIntents = alternateIntents;
    this.workspaceId = workspaceId;
    this.retries = retries;
    this.delay = delay;
  }

  @Override
  public MessageResponse message(Map<String, Object> contextMap, String conversationId, String text)
      throws ApplicationException {
    Context context = new Context();
    context.setConversationId(conversationId);
    context.setProperties(contextMap);

    MessageOptions options = new MessageOptions.Builder()
            .alternateIntents(this.alternateIntents)
            .workspaceId(this.workspaceId)
            .context(context)
            .input(new MessageInput.Builder()
                    .text(text)
                    .build())
            .build();

    MessageResponse messageResponse = null;
    RetryStrategy retry = new RetryStrategy(this.retries, this.delay, ErrorCode.WATSON_ASSISTANT_ERROR);
    while (retry.shouldRetry()) {
      try {
        log.info("Executing Watson Assistant Service, ConversationId={}", conversationId);
        long start = System.currentTimeMillis();
        messageResponse = this.assistant.message(options).execute().getResult();
        long elapsedTime = System.currentTimeMillis() - start;
        log.info("Message received from Watson Assistant, {} ms", elapsedTime);
        break;
      } catch (Exception e) {
        log.error("An error has occurred invoking Watson Assistant Service", e);
        try {
          retry.errorOccurred("[Watson Assistant Service] " + e.getMessage());
        } catch (RetryAttemptsReachedException ex) {
          throw new ApplicationException("An error has occurred invoking Watson Assistant Service", ex, ex.getErrorCode());
        }
      }
    }
    return messageResponse;
  }
}
