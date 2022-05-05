package com.va.orchestrator.api.service.customer;

import com.va.orchestrator.api.service.customer.factory.Operation;
import com.va.orchestrator.api.service.customer.factory.OperationType;

import lombok.extern.slf4j.Slf4j;

import okhttp3.HttpUrl;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

/** @author huerta.jorge at gmail.com */
@Slf4j
@Component
public class BarOperation implements Operation {

  private final RestTemplate restTemplate;

  public BarOperation(RestTemplateBuilder restTemplateBuilder) {
    this.restTemplate = restTemplateBuilder.build();
  }

  @Override
  public Map<String, Object> fireOperation(Map<String, Object> map) {
    log.info("Executing {}, fireOperation method", this.getClass().getSimpleName());

    HttpUrl httpUrl = new HttpUrl.Builder()
            .scheme("https")
            .host("jsonplaceholder.typicode.com")
            .port(443)
            .addPathSegment("posts")
            .build();

    ResponseEntity<List<Object>> response =
        restTemplate.exchange(
            httpUrl.uri(), HttpMethod.GET, null, new ParameterizedTypeReference<List<Object>>() {});

    map.put("response", response.getBody());
    return map;
  }

  @Override
  public OperationType getOperationType() {
    return OperationType.BAR_OPERATION;
  }
}
