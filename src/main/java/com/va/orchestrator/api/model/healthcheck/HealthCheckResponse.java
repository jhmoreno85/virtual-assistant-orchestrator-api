package com.va.orchestrator.api.model.healthcheck;

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
public class HealthCheckResponse {
  private String status;
}
