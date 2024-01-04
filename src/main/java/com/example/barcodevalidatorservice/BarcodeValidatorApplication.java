package com.example.barcodevalidatorservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@Import(BarcodeValidationController.class)
@SpringBootApplication()
public class BarcodeValidatorApplication {

    public static void main(String[] args) {
        // There are no arguments, so ignore them.
        SpringApplication.run(BarcodeValidatorApplication.class);
    }
}
