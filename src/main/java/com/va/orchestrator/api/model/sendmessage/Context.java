package com.va.orchestrator.api.model.sendmessage;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * @author huerta.jorge at gmail.com
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@EqualsAndHashCode
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Context {
  @JsonProperty("conversation_id")
  private String conversationId;

  @JsonProperty("communication_context")
  private CommunicationContext communicationContext;

  @JsonProperty("security_context")
  private SecurityContext securityContext;
}
