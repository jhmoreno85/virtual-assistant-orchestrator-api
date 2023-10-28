package com.va.orchestrator.api.model.sendmessage;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author huerta.jorge at gmail.com
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Channel {
  @JsonProperty("channel_type")
  private String channelType;

  @JsonProperty("channel_subtype")
  private String channelSubtype;
}
