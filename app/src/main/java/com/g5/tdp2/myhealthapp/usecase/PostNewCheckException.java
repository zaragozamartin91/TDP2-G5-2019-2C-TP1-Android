package com.g5.tdp2.myhealthapp.usecase;

public class PostNewCheckException extends RuntimeException {
    public PostNewCheckException(String message) {
        super(message);
    }

    public PostNewCheckException(String message, Throwable cause) {
        super(message, cause);
    }
}
