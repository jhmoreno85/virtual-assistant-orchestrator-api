package com.va.orchestrator.api.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.va.orchestrator.api.exception.BadRequestException;
import com.va.orchestrator.api.exception.ErrorCode;
import com.va.orchestrator.api.model.sendmessage.CommunicationContext;
import com.va.orchestrator.api.model.sendmessage.Context;
import com.va.orchestrator.api.model.sendmessage.SendMessageRequest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author huerta.jorge at gmail.com
 */
public class ContextUtil {

  private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
  private static final String TRANSACTION_CONTEXT_OPERATION_PATH = "transaction_context.request.operation";
  private static final String REGEX_ARRAY_BRACKETS = "^\\[\\d+]?";

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

  public static <T> Optional<T> getTargetObjectFromMap(Map<String, Object> map, String targetPath, Class<T> clazz) {
    if (null == targetPath || targetPath.isEmpty()) {
      return Optional.empty();
    }
    return Optional.ofNullable(clazz.cast(getTargetObjectFromMap(map, targetPath.split("\\."), 0)));
  }

  @SuppressWarnings("unchecked")
  private static Object getTargetObjectFromMap(Object object, String[] targetPath, int currIndex) {
    if (object instanceof Map) {
      Map<String, Object> map = (Map<String, Object>) object;
      if (map.containsKey(targetPath[currIndex])) {
        return currIndex == targetPath.length - 1
            ? map.get(targetPath[currIndex])
            : getTargetObjectFromMap(map.get(targetPath[currIndex]), targetPath, currIndex + 1);
      }
    } else if (object instanceof List && targetPath[currIndex].matches(REGEX_ARRAY_BRACKETS)) {
      List<Object> list = (List<Object>) object;
      int elementIndex = Integer.parseInt(targetPath[currIndex].substring(1, targetPath[currIndex].length() - 1));
      if (elementIndex < list.size()) {
        return currIndex == targetPath.length - 1 ?
                list.get(elementIndex) :
                getTargetObjectFromMap(list.get(elementIndex), targetPath, currIndex + 1);
      }
    }
    return null;
  }

  public static void populateCommunicationContext(Map<String, Object> context, SendMessageRequest sendMessageRequest) throws BadRequestException {
    String[] channelTypeAndSubtype = Optional.ofNullable(sendMessageRequest)
            .map(SendMessageRequest::getContext)
            .map(Context::getCommunicationContext)
            .map(CommunicationContext::getChannel)
            .filter(ch -> null != ch.getChannelType() && null != ch.getChannelSubtype())
            .map(ch -> {
                  ch.setChannelType(ch.getChannelType().trim());
                  ch.setChannelSubtype(ch.getChannelSubtype().trim());
                  return ch;
                })
            .filter(ch -> !ch.getChannelType().isEmpty() && !ch.getChannelSubtype().isEmpty())
            .map(ch -> new String[] {ch.getChannelType(), ch.getChannelSubtype()})
            .orElseThrow(() -> new BadRequestException("Communication Context - Channel Type/Subtype is null or empty", new Exception(), ErrorCode.INVALID_PAYLOAD));
    context.put("communication_context", createCommunicationContext(channelTypeAndSubtype[0], channelTypeAndSubtype[1]));
  }

  private static Map<String, Object> createCommunicationContext(String channelType, String channelSubtype) {
    Map<String, Object> channel = new HashMap<>();
    channel.put("channel_type", channelType);
    channel.put("channel_subtype", channelSubtype);

    Map<String, Object> communicationContext = new HashMap<>();
    communicationContext.put("channel", channel);
    return communicationContext;
  }

  public static Optional<String> getOperation(Map<String, Object> map) {
    return getTargetObjectFromMap(map, TRANSACTION_CONTEXT_OPERATION_PATH, String.class)
        .map(String::trim)
        .filter(s -> !s.isEmpty());
  }
}
