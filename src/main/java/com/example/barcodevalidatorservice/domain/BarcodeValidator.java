/**
 *
 */
package com.example.barcodevalidatorservice.domain;

import java.util.List;

/**
 * Interface for all barcode validator implementations.
 */
public interface BarcodeValidator <T> {

    /**
     * Validate the barcode.
     * @param barcode
     *          The barcode to validate.
     * @param errors
     *          Populated with any validation errors.
     * @return
     *          {code true} if {@code barcode} is valid. Otherwise, {@code false} and {@code errors} is populated with the errors.
     */
    public boolean validate(T barcode, List<String> errors);
}
