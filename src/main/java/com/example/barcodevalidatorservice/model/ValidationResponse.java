package com.example.barcodevalidatorservice.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * <p>The response consists of:</p>
 * <ul>
 *  <li>The barcode that was validated (or not).</li>
 *  <li>One or more messages.</li>
 *  <li>The validation status {code true} or {@code false}.
 * </ul>
 */
@JsonIgnoreProperties(ignoreUnknown = false)
public record ValidationResponse(String s10Barcode, List<String> messages, boolean status) {
}
