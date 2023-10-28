package com.va.orchestrator.api.util;

import com.ibm.watson.assistant.v1.model.MessageResponse;

import com.va.orchestrator.api.model.sendmessage.Context;
import com.va.orchestrator.api.model.sendmessage.Input;
import com.va.orchestrator.api.model.sendmessage.Output;
import com.va.orchestrator.api.model.sendmessage.SendMessageResponse;

/**
 * @author huerta.jorge at gmail.com
 */
public class EventFlowUtil {

  private EventFlowUtil() {
    throw new IllegalStateException("EvenFlow Util class");
  }

  public static SendMessageResponse createSendMessageResponse(MessageResponse messageResponse, String inputText) {
    return SendMessageResponse.builder()
            .context(Context.builder()
                    .conversationId(messageResponse.getContext().getConversationId())
                    .build())
            .input(Input.builder()
                    .text(inputText)
                    .build())
            .output(Output.builder()
                    .text(messageResponse.getOutput().getText())
                    .build())
            .build();
  }
}
