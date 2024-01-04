package com.example.barcodevalidatorservice;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.barcodevalidatorservice.model.ValidationRequest;
import com.example.barcodevalidatorservice.model.ValidationResponse;


@RestController
public interface IBarcodeValidationController {

    @PostMapping("/validate")
    ResponseEntity<ValidationResponse> validate(@RequestBody ValidationRequest validationRequest);

}
