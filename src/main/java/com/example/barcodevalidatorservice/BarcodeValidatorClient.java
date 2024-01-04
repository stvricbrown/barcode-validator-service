package com.example.barcodevalidatorservice;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClient.Builder;

import com.example.barcodevalidatorservice.model.ValidationRequest;
import com.example.barcodevalidatorservice.model.ValidationResponse;

import reactor.core.publisher.Mono;

@Component
public class BarcodeValidatorClient {

    private final WebClient client;

    public BarcodeValidatorClient(Builder builder) {
        client = builder.baseUrl("http://localhost:8080").build();
    }

    public Mono<ValidationResponse> validate(ValidationRequest validationRequest) {
        return client.post().uri("/validate").accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(ValidationResponse.class);
    }

}
