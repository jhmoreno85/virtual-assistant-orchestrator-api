package com.va.orchestrator.api.model.dao;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;

/**
 * @author huerta.jorge at gmail.com
 */
@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserConversation {

  @Id
  @Column(name = "CONVERSATION_ID")
  private String conversationId;

  @Column(name = "START_TS")
  private Date startTs;

  @Column(name = "CONTEXT")
  private String context;
}
