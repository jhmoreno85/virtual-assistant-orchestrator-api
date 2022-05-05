package com.va.orchestrator.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@PropertySource(value = "classpath:application.yml")
public class VirtualAssistantOrchestratorApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(VirtualAssistantOrchestratorApiApplication.class, args);
    }

}
