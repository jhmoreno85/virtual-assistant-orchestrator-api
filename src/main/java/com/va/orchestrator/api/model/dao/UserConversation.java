package com.va.orchestrator.api.model.dao;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;

/**
 * @author huerta.jorge at gmail.com
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
public class UserConversation {

  @Id
  @Column(name = "CONVERSATION_ID")
  private String conversationId;

  @Column(name = "START_TS")
  private Date startTs;

  @Column(name = "CONTEXT")
  private String context;
}
