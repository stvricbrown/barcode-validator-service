package com.example.barcodevalidatorservice.domain;

import static java.lang.Integer.parseInt;
import static java.lang.String.format;
import static java.util.regex.Pattern.compile;
import static org.apache.commons.lang3.StringUtils.isEmpty;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Validates an S10 barcode string.
 *
 * For full details, see the <a href=""https://en.wikipedia.org/wiki/S10_(UPU_standard)">S10_(UPU_standard)</a>.
 */
public class S10BarcodeValidator implements BarcodeValidator<String> {

    private static final int SERIAL_NUMBER_LENGTH = 8;

    private static final String EXPECTED_COUNTRY_CODE = "GB";

    /**
     * The regular expression for an S10 barcode.
     */
    private static final String S10_BARCODE_EXPRESSION = "^(?<serviceCode>[A-Z]{2})(?<serialNumber>[\\d]{8})" +
                                                         "(?<checkDigit>[\\d])(?<countryCode>[A-Z]{2}$)";

    static final String DESCRIPTION = "<two letter service code><eight digit serial number><check digit>" +
                                      "<two letter country code>";

    private static final Pattern S10_BARCODE_PATTERN;

    private static final int[] CHECK_DIGIT_WEIGHTS = {
        8, 6, 4, 2, 3, 5, 9, 7
    };

    static {
        S10_BARCODE_PATTERN = compile(S10_BARCODE_EXPRESSION);
    }

    @Override
    public boolean validate(String s10Barcode, List<String> messages) {
        if (isEmpty(s10Barcode)) {
            messages.add("An S10 barcode cannnot be null or empty.");
            return false;
        }

        Matcher matcher = S10_BARCODE_PATTERN.matcher(s10Barcode);
        boolean matches = matcher.matches();

        if (matches) {
            return validateS10BarcodeParts(matcher, messages);
        }

        String message = format("The S10 barcode \"%s\" does not match %s.", s10Barcode, DESCRIPTION);
        messages.add(message);
        return false;
    }

    private boolean validateS10BarcodeParts(Matcher matcher, List<String> messages) {
        int errorCount = 0;

        String s10Barcode = matcher.group(0);

        if (!validateCheckDigit(matcher, messages)) {
            ++errorCount;
        }

        if (!validateCountryCode(s10Barcode, matcher.group("countryCode"), messages)) {
            ++errorCount;
        }

        if (errorCount == 0) {
            String message = format("The S10 barcode \"%s\" is a valid S10 barcode.", s10Barcode);
            messages.add(message);
            return true;
        }

        return  false;
    }

    private boolean validateCountryCode(String s10Barcode, String countryCode, List<String> messages) {

        if (EXPECTED_COUNTRY_CODE.equals(countryCode)) {
            return true;
        }
        String message = format("The country code \"%s\" in the S10 barcode \"%s\" must be \"%s\".",
                                countryCode, s10Barcode, EXPECTED_COUNTRY_CODE);
        messages.add(message);
        return  false;
    }

    private boolean validateCheckDigit(Matcher matcher, List<String> messages) {

        String serialNumber = matcher.group("serialNumber");
        int expectedCheckDigit = parseInt(matcher.group("checkDigit"));

        int calculatedCheckDigit = calculateCheckDigit(serialNumber);
        if (calculatedCheckDigit == expectedCheckDigit) {
            return true;
        }

        String s10Barcode = matcher.group(0);
        String message = format("The check digit \"%s\" in the S10 barcode \"%s\" should be \"%s\".",
                                expectedCheckDigit, s10Barcode, calculatedCheckDigit);
        messages.add(message);
        return false;
    }

    private int calculateCheckDigit(String serialNumber) {

        int sumOfProducts = 0;
        for (int index = 0; index < SERIAL_NUMBER_LENGTH; index++) {
            sumOfProducts += (serialNumber.charAt(index) - '0') * CHECK_DIGIT_WEIGHTS[index];
        }

        int checkDigit = 11 - sumOfProducts % 11;

        return switch(checkDigit) {
            case 10 -> 0;
            case 11 -> 5;
            default -> checkDigit;
        };
    }
}
