package com.example.barcodevalidatorservice;


import static java.util.Collections.unmodifiableList;
import static org.springframework.http.HttpStatus.OK;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.ResponseEntity;

import com.example.barcodevalidatorservice.domain.BarcodeValidator;
import com.example.barcodevalidatorservice.domain.S10BarcodeValidator;
import com.example.barcodevalidatorservice.model.ValidationRequest;
import com.example.barcodevalidatorservice.model.ValidationResponse;

public class BarcodeValidationController implements IBarcodeValidationController {


    @Override
    public ResponseEntity<ValidationResponse> validate(ValidationRequest validationRequest) {
        BarcodeValidator<String> barcodeValidator = new S10BarcodeValidator();

        String s10Barcode = validationRequest.s10Barcode();
        List<String> messages = new ArrayList<>();
        boolean isBarcodeValid = barcodeValidator.validate(s10Barcode, messages);
        ValidationResponse responseBody = new ValidationResponse(s10Barcode, unmodifiableList(messages), isBarcodeValid);

        return new ResponseEntity<>(responseBody, OK);
    }
}
