package com.va.orchestrator.api.model.dao;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

/**
 * @author huerta.jorge at gmail.com
 */
@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserConversationDetailed {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "ID")
  private Long id;

  @Column(name = "CONVERSATION_ID")
  private String conversationId;

  @Column(name = "TRANSACTION_TS")
  @Temporal(TemporalType.TIMESTAMP)
  private Date transactionTs;

  @Column(name = "INPUT_TEXT")
  private String inputText;

  @Column(name = "OUTPUT_TEXT")
  private String outputText;

  @Column(name = "CONFIDENCE")
  private Double confidence;

  @Column(name = "INTENTS")
  private String intents;

  @Column(name = "ENTITIES")
  private String entities;

  @Column(name = "FEEDBACK")
  private String feedback;
}
