package com.example.barcodevalidatorservice;


import static java.lang.String.format;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsIn.in;
import static org.hamcrest.core.Every.everyItem;
import static org.springframework.http.MediaType.APPLICATION_JSON;

import java.io.UnsupportedEncodingException;
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
    void testValidBarcode(String expectedS10Barcode, List<String> expectedMessages) {

        // Given
        ValidationRequest validationRequest = new ValidationRequest(expectedS10Barcode);

        // When, Then
        doTestAndVerifyResponse(expectedS10Barcode, validationRequest, true, expectedMessages);

    }

    @ParameterizedTest
    @MethodSource("provideInvalidS10Barcodes")
    void testInvalidBarcode(String expectedS10Barcode, List<String> expectedMessages) {

        // Given
        ValidationRequest validationRequest = new ValidationRequest(expectedS10Barcode);

        // When, Then
        doTestAndVerifyResponse(expectedS10Barcode, validationRequest, false, expectedMessages);

    }

    private void doTestAndVerifyResponse(String expectedS10Barcode,
                                         ValidationRequest validationRequest,
                                         boolean isBarcodeValid,
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
                     .consumeWith(responseExchangeResult -> verifyResponseBody(responseExchangeResult,
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

        String reason = format("The barcode \"%s\" is valid.", expectedS10Barcode);
        assertThat(reason, validationResponse.status(), is(equalTo(isBarcodeValid)));
        reason = format("The barcode must be \"%s\".", expectedS10Barcode);
        assertThat(reason, validationResponse.s10Barcode(), is(equalTo(expectedS10Barcode)));
        assertThat("All the expected number of messages should be present.",
                   validationResponse.messages(), everyItem(is(in(expectedMessages))));
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
