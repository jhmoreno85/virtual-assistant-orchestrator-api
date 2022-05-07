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

  public static SendMessageResponse createSendMessageResponse(
      MessageResponse messageResponse, String inputText) {
    Input input = new Input();
    input.setText(inputText);

    Output output = new Output();
    output.setText(messageResponse.getOutput().getText());

    Context context = new Context();
    context.setConversationId(messageResponse.getContext().getConversationId());

    SendMessageResponse sendMessageResponse = new SendMessageResponse();
    sendMessageResponse.setContext(context);
    sendMessageResponse.setInput(input);
    sendMessageResponse.setOutput(output);
    return sendMessageResponse;
  }
}
