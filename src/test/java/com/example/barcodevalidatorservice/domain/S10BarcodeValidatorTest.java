package com.example.barcodevalidatorservice.domain;

import static com.example.barcodevalidatorservice.domain.S10BarcodeValidator.DESCRIPTION;
import static java.lang.String.format;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.collection.IsIn.in;
import static org.hamcrest.core.Every.everyItem;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import com.example.test.ValidatorTestBase;

class S10BarcodeValidatorTest extends ValidatorTestBase {

    private S10BarcodeValidator objectUnderTest;

    private List<String> errorMessages;

    @BeforeEach
    void beforeEachTestMethod() {
        objectUnderTest = new S10BarcodeValidator();
        errorMessages = new ArrayList<>();
    }

    @ParameterizedTest
    @MethodSource("provideInvalidS10Barcodes")
    void testS10BarcodeParts(String s10Barcode, List<String> expectedMessages) {

        // Given

        // When
        boolean isValidS10Barcode = objectUnderTest.validate(s10Barcode, errorMessages);

        // Then
        String reason = format("The S10 barcode \"%s\" should not be valid.", s10Barcode);
        assertThat(reason, isValidS10Barcode == false);

        assertThat("There should be the expected number of messages.",
                   errorMessages, hasSize(expectedMessages.size()));

        assertThat("All the expected number of messages should be present.",
                   errorMessages, everyItem(is(in(expectedMessages))));
    }

    @ParameterizedTest
    @ValueSource(strings = {
         " ",
         "A473124829GB", "473124829GB", "AAA473124829GB", "aa473124829GB", "aA473124829GB",
         "AA4731248x9GB", "AA47312482xGB",
         "AA473124829gb", "AA473124829G", "AA473124829"
    })
    void testS10BarcodeRegularExpression(String s10Barcode) {

        // Given
        List<String> expectedMessages = new ArrayList<>();
        expectedMessages.add(format("The S10 barcode \"%s\" does not match %s.", s10Barcode, DESCRIPTION));

        // When
        boolean isValidS10Barcode = objectUnderTest.validate(s10Barcode, errorMessages);

        // Then
        String reason = format("The S10 barcode \"%s\" should not be valid.", s10Barcode);
        assertThat(reason, isValidS10Barcode == false);

        assertThat("There should be exactly one message",
                   errorMessages, is(equalTo(expectedMessages)));
    }

    @ParameterizedTest
    @NullSource
    @EmptySource
    void testValidateNullEmpty(String s10Barcode) {

        // Given
        List<String> expectedMessages = new ArrayList<>();
        expectedMessages.add("An S10 barcode cannnot be null or empty.");

        // When
        boolean isValidS10Barcode = objectUnderTest.validate(s10Barcode, errorMessages);

        // Then
        String reason = format("The S10 barcode \"%s\" is not valid.", s10Barcode);
        assertThat(reason, isValidS10Barcode == false);

        assertThat("There should be exactly one message",
                   errorMessages, is(equalTo(expectedMessages)));
    }

    @ParameterizedTest
    @MethodSource("provideValidS10Barcodes")
    void testValidateOK(String s10Barcode, List<String> expectedMessages) {

        // Given

        // When
        boolean isValidS10Barcode = objectUnderTest.validate(s10Barcode, errorMessages);

        // Then
        String reason = format("The S10 barcode \"%s\" should be valid.", s10Barcode);
        assertThat(reason, isValidS10Barcode == true);

        assertThat("There should be exactly one message",
                   errorMessages, is(equalTo(expectedMessages)));
    }
}
