package com.kameleoon.testproject.quote.exceptions;

public class QuoteNotFoundException extends RuntimeException {

    public QuoteNotFoundException(String message) {
        super(message);
    }
}
