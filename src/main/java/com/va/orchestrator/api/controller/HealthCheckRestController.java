package com.va.orchestrator.api.controller;

import com.va.orchestrator.api.model.healthcheck.HealthCheckResponse;

import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** @author huerta.jorge at gmail.com */
@Slf4j
@RestController
@RequestMapping("/healthCheck")
public class HealthCheckRestController {

  @GetMapping(
      value = "/v1/ping",
      produces = {MediaType.APPLICATION_JSON_VALUE})
  public ResponseEntity<HealthCheckResponse> ping() {
    log.info("Inside of [ping]");
    return new ResponseEntity<>(new HealthCheckResponse("success"), HttpStatus.OK);
  }
}
