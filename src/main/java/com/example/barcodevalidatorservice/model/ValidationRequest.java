package com.example.barcodevalidatorservice.model;

import static org.apache.commons.lang3.builder.ToStringStyle.SHORT_PREFIX_STYLE;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * The only parameter is the barcode to validate.
 */
@JsonIgnoreProperties(ignoreUnknown = false)
public class ValidationRequest {

    private final String s10Barcode;

    @JsonCreator
    public ValidationRequest(String s10Barcode) {
        this.s10Barcode = s10Barcode;
    }

    public final String getS10Barcode() {
        return s10Barcode;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (other instanceof ValidationRequest otherCreateRequest) {
            return s10Barcode.equals(otherCreateRequest.getS10Barcode());
        }

        return false;
    }

    @Override
    public int hashCode() {
        return s10Barcode.hashCode();
    }

    @Override
    public String toString() {
        ToStringBuilder builder = new ToStringBuilder(this, SHORT_PREFIX_STYLE);

        return builder.append("s10Barcode", s10Barcode)
                      .build();
    }
}
