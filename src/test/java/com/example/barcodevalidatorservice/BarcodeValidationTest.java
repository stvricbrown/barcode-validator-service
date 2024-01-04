package com.example.barcodevalidatorservice;


import static java.lang.String.format;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.springframework.http.MediaType.APPLICATION_JSON;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.opentest4j.AssertionFailedError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.example.barcodevalidatorservice.model.ValidationRequest;
import com.example.barcodevalidatorservice.model.ValidationResponse;
import com.example.test.ValidatorTestBase;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import reactor.core.publisher.Mono;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class BarcodeValidationTest extends ValidatorTestBase {


    @Autowired
    ConfigurableApplicationContext context;

    @Autowired
    private WebTestClient webTestClient;

    @ParameterizedTest
    @MethodSource("provideValidS10Barcodes")
    void testValidBarcode() {

        // Given
        String expectedS10Barcode =  "AB473124829GB";
        ValidationRequest validationRequest = new ValidationRequest(expectedS10Barcode);
        boolean isBarcodeValid = true;
        String expectedMessage = format("The S10 barcode \"%s\" is a valid S10 barcode.", expectedS10Barcode);

        List<String> expectedMessages = new ArrayList<>();
        expectedMessages.add(expectedMessage);

        doTestAndVerifyResponse(expectedS10Barcode, validationRequest, isBarcodeValid, expectedMessages);

    }

    @ParameterizedTest
    @MethodSource("provideInvalidS10Barcodes")
    void testInvalidBarcode() {

        // Given
        String expectedS10Barcode =  "AB473124829GB";
        ValidationRequest validationRequest = new ValidationRequest(expectedS10Barcode);
        boolean isBarcodeValid = true;
        String expectedMessage = format("The S10 barcode \"%s\" is a valid S10 barcode.", expectedS10Barcode);

        List<String> expectedMessages = new ArrayList<>();
        expectedMessages.add(expectedMessage);

        doTestAndVerifyResponse(expectedS10Barcode, validationRequest, isBarcodeValid, expectedMessages);

    }

    private void doTestAndVerifyResponse(String expectedS10Barcode, ValidationRequest validationRequest, boolean isBarcodeValid,
                    List<String> expectedMessages) {

        // When
        webTestClient.post().uri("/validate")
                     .contentType(APPLICATION_JSON)
                     .accept(APPLICATION_JSON)
                     .body(Mono.just(validationRequest), ValidationRequest.class)
                     .exchange()

        // Then
                     .expectStatus().isOk()
                     .expectHeader().contentType(APPLICATION_JSON)
                     .expectBody()
                     .consumeWith(responseExchangeResult ->
                         this.verifyResponseBody(responseExchangeResult,
                                                 isBarcodeValid,
                                                 expectedS10Barcode,
                                                 expectedMessages));
    }

    private void verifyResponseBody(EntityExchangeResult<byte[]> responseExchangeResult,
                                    boolean isBarcodeValid,
                                    String expectedS10Barcode,
                                    List<String> expectedMessages) {
        byte[] responseContent = responseExchangeResult.getResponseBodyContent();

        assertThat("There must be a response body.", responseContent, is(not(nullValue())));
        assertThat("The response body must not be empty.", responseContent.length, is(not(equalTo(0))));

        String json;
        try {
            json = new String(responseContent, "UTF-8");
        } catch (UnsupportedEncodingException use) {
            throw new AssertionFailedError("Failed read the response", use);
        }

        ValidationResponse validationResponse = convertJsonToPojo(json);

        String message = format("The barcode \"%s\" is valid.", expectedS10Barcode);
        assertThat(message, validationResponse.getStatus(), is(equalTo(isBarcodeValid)));
        message = format("The barcode must be \"%s\".", expectedS10Barcode);
        assertThat(message, validationResponse.getS10Barcode(), is(equalTo(expectedS10Barcode)));
        message = "The messages must be expected messages.";
        assertThat(message, validationResponse.getMessages(), is(equalTo(expectedMessages)));

    }

    private ValidationResponse convertJsonToPojo(String jsonString) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(jsonString, ValidationResponse.class);
        } catch (JsonProcessingException jpe) {
            throw new AssertionFailedError("Failed to convert JSON to response", jpe);
        }
    }
}
