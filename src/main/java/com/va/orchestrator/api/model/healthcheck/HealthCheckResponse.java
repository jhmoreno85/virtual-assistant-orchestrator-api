package com.va.orchestrator.api.model.healthcheck;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/** @author huerta.jorge at gmail.com */
@Getter
@Setter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
public class HealthCheckResponse {
  private String status;
}
