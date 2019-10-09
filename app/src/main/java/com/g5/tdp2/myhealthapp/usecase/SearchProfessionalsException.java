package com.g5.tdp2.myhealthapp.usecase;

public class SearchProfessionalsException extends RuntimeException {
    public SearchProfessionalsException(String message) {
        super(message);
    }

    public SearchProfessionalsException(String message, Throwable cause) {
        super(message, cause);
    }
}
