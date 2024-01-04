package com.example.test;

import java.util.ArrayList;
import java.util.stream.Stream;

import org.junit.jupiter.params.provider.Arguments;

public abstract class ValidatorTestBase {


    protected static Stream<Arguments> provideInvalidS10Barcodes() {
        return Stream.of(
          Arguments.of("AB473124819GB", new ArrayList<String>() {
              private static final long serialVersionUID = 1L; {
              add("The check digit \"9\" in the S10 barcode \"AB473124819GB\" should be \"5\".");
          }})
          ,
          Arguments.of("AB473124828GB", new ArrayList<String>() {
              private static final long serialVersionUID = 1L; {
              add("The check digit \"8\" in the S10 barcode \"AB473124828GB\" should be \"9\".");
          }})
          ,
          Arguments.of("AB473124829FR", new ArrayList<String>() {
              private static final long serialVersionUID = 1L; {
              add("The country code \"FR\" in the S10 barcode \"AB473124829FR\" must be \"GB\".");
          }})
          ,
          Arguments.of("AB473124828FR", new ArrayList<String>() {
              private static final long serialVersionUID = 1L; {
              add("The country code \"FR\" in the S10 barcode \"AB473124828FR\" must be \"GB\".");
              add("The check digit \"8\" in the S10 barcode \"AB473124828FR\" should be \"9\".");
          }})
        );
    }


    protected static Stream<Arguments> provideValidS10Barcodes() {

        return Stream.of(
          Arguments.of("AB473124829GB", new ArrayList<String>() {
              private static final long serialVersionUID = 1L; {
              add("The S10 barcode \"AB473124829GB\" is a valid S10 barcode.");
          }})
          ,
          Arguments.of( "AA003000000GB", new ArrayList<String>() {
              private static final long serialVersionUID = 1L; {
              add("The S10 barcode \"AA003000000GB\" is a valid S10 barcode.");
          }})
          ,
          Arguments.of("AA002010005GB", new ArrayList<String>() {
              private static final long serialVersionUID = 1L; {
                  add("The S10 barcode \"AA002010005GB\" is a valid S10 barcode.");
          }})
        );
    }

}
