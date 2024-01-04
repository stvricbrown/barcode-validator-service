package com.example.barcodevalidatorservice.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * <p>The response consists of:</p>
 * <ul>
 *  <li>The barcode that was validated (or not).</li>
 *  <li>One or more messages.</li>
 *  <li>The validation status {code true} or {@code false}.
 * </ul>
 */
@JsonIgnoreProperties(ignoreUnknown = false)
public class ValidationResponse {

    private final String s10Barcode;

    private final List<String> messages;

    private final boolean status;

    @JsonCreator
    public ValidationResponse(@JsonProperty(value="s10barcode", required=true) String s10Barcode,
                              @JsonProperty(value="messages", required=true) List<String> messages,
                              @JsonProperty(value="status", required=true) boolean status) {
        this.s10Barcode = s10Barcode;
        this.messages = messages;
        this.status = status;
    }

    @JsonGetter("s10barcode")
    public final String getS10Barcode() {
        return s10Barcode;
    }

    @JsonGetter("messages")
    public final List<String> getMessages() {
        return messages;
    }

    @JsonGetter("status")
    public final boolean getStatus() {
        return status;
    }
}
