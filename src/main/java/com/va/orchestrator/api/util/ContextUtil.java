package com.va.orchestrator.api.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.va.orchestrator.api.exception.BadRequestException;
import com.va.orchestrator.api.exception.ErrorCode;
import com.va.orchestrator.api.model.sendmessage.Channel;
import com.va.orchestrator.api.model.sendmessage.CommunicationContext;
import com.va.orchestrator.api.model.sendmessage.Context;
import com.va.orchestrator.api.model.sendmessage.SendMessageRequest;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * @author huerta.jorge at gmail.com
 */
public class ContextUtil {

  private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
  private static final int COMMUNICATION_CONTEXT_CAPACITY = 1;
  private static final int COMMUNICATION_CONTEXT_CHANNEL_CAPACITY = 2;
  private static final String TRANSACTION_CONTEXT_OPERATION_PATH =
      "transaction_context.request.operation";

  private ContextUtil() {
    throw new IllegalStateException("Context Util class");
  }

  public static Map<String, Object> jsonStringToMap(String jsonString) {
    try {
      return OBJECT_MAPPER.readValue(jsonString, new TypeReference<Map<String, Object>>() {});
    } catch (Exception e) {
      return new HashMap<>();
    }
  }

  public static String mapToJsonString(Map<String, Object> map) {
    try {
      return OBJECT_MAPPER.writeValueAsString(map);
    } catch (Exception e) {
      return "{}";
    }
  }

  public static <T> Optional<T> getTargetObjectFromMap(
      Map<String, Object> map, String targetPath, Class<T> clazz) {
    if (null == targetPath || targetPath.isEmpty() || null == map || map.isEmpty()) {
      return Optional.empty();
    }
    return Optional.ofNullable(clazz.cast(getTargetObjectFromMap(map, targetPath.split("\\."), 0)));
  }

  @SuppressWarnings("unchecked")
  private static Object getTargetObjectFromMap(
      Map<String, Object> map, String[] targetPath, int currIndex) {
    if (map.containsKey(targetPath[currIndex])) {
      if (currIndex == targetPath.length - 1) {
        return map.get(targetPath[currIndex]);
      } else {
        return getTargetObjectFromMap(
            (Map<String, Object>) map.get(targetPath[currIndex]), targetPath, currIndex + 1);
      }
    }
    return null;
  }

  public static void populateCommunicationContext(
      Map<String, Object> context, SendMessageRequest sendMessageRequest)
      throws BadRequestException {
    String channelType =
        Optional.of(sendMessageRequest)
            .map(SendMessageRequest::getContext)
            .map(Context::getCommunicationContext)
            .map(CommunicationContext::getChannel)
            .map(Channel::getChannelType)
            .map(String::trim)
            .filter(currChannelType -> !currChannelType.isEmpty())
            .orElseThrow(
                () ->
                    new BadRequestException(
                        "Communication Context - Channel Type is null or empty",
                        new Exception(),
                        ErrorCode.INVALID_PAYLOAD));
    String channelSubtype =
        Optional.of(sendMessageRequest)
            .map(SendMessageRequest::getContext)
            .map(Context::getCommunicationContext)
            .map(CommunicationContext::getChannel)
            .map(Channel::getChannelSubtype)
            .map(String::trim)
            .filter(currChannelSubtype -> !currChannelSubtype.isEmpty())
            .orElseThrow(
                () ->
                    new BadRequestException(
                        "Communication Context - Channel Subtype is null or empty",
                        new Exception(),
                        ErrorCode.INVALID_PAYLOAD));
    context.put("communication_context", createCommunicationContext(channelType, channelSubtype));
  }

  private static Map<String, Object> createCommunicationContext(
      String channelType, String channelSubtype) {
    Map<String, Object> channel = new HashMap<>(COMMUNICATION_CONTEXT_CHANNEL_CAPACITY);
    channel.put("channel_type", channelType);
    channel.put("channel_subtype", channelSubtype);

    Map<String, Object> communicationContext = new HashMap<>(COMMUNICATION_CONTEXT_CAPACITY);
    communicationContext.put("channel", channel);
    return communicationContext;
  }

  public static Optional<String> getOperation(Map<String, Object> map) {
    return getTargetObjectFromMap(map, TRANSACTION_CONTEXT_OPERATION_PATH, String.class)
        .map(String::trim)
        .filter(s -> !s.isEmpty());
  }
}
