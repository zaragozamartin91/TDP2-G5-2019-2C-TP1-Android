package com.g5.tdp2.myhealthapp.usecase;

public class GetChecksException extends RuntimeException {
    public GetChecksException(String message) {
        super(message);
    }

    public GetChecksException(String message, Throwable cause) {
        super(message, cause);
    }
}
