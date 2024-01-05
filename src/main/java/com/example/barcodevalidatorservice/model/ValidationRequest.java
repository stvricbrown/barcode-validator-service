package com.example.barcodevalidatorservice.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * The only parameter is the barcode to validate.
 */
@JsonIgnoreProperties(ignoreUnknown = false)
public record ValidationRequest(String s10Barcode) {
}
