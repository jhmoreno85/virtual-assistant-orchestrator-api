package com.va.orchestrator.api.controller;

import com.va.orchestrator.api.exception.ApplicationException;
import com.va.orchestrator.api.exception.BadRequestException;
import com.va.orchestrator.api.model.ErrorResponse;
import com.va.orchestrator.api.model.sendmessage.SendMessageRequest;
import com.va.orchestrator.api.model.sendmessage.SendMessageResponse;
import com.va.orchestrator.api.service.eventflow.EventFlowService;

import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author huerta.jorge at gmail.com
 */
@Slf4j
@RestController
@RequestMapping("/conversation")
public class ConversationRestController {

  private final EventFlowService eventFlowService;

  public ConversationRestController(EventFlowService eventFlowService) {
    this.eventFlowService = eventFlowService;
  }

  @PostMapping(
      value = "/v1/sendMessage",
      consumes = {MediaType.APPLICATION_JSON_VALUE},
      produces = {MediaType.APPLICATION_JSON_VALUE})
  public ResponseEntity<Object> sendMessage(@RequestBody SendMessageRequest payload) {
    log.info("Inside of [sendMessage]");
    try {
      SendMessageResponse response = eventFlowService.sendMessage(payload);
      return new ResponseEntity<>(response, HttpStatus.OK);
    } catch (BadRequestException e) {
      log.error("An error has occurred: ", e);
      ErrorResponse errorResponse = new ErrorResponse(e.getErrorCode().getCode(), e.getMessage());
      return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    } catch (ApplicationException e) {
      log.error("An error has occurred: ", e);
      ErrorResponse errorResponse = new ErrorResponse(e.getErrorCode().getCode(), e.getMessage());
      return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }
}
