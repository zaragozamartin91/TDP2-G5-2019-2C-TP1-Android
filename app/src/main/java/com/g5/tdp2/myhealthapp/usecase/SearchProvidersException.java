package com.g5.tdp2.myhealthapp.usecase;

public class SearchProvidersException extends RuntimeException {
    public SearchProvidersException(String message) {
        super(message);
    }

    public SearchProvidersException(String message, Throwable cause) {
        super(message, cause);
    }
}
