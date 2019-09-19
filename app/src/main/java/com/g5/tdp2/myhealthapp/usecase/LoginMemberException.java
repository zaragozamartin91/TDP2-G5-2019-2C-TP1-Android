package com.g5.tdp2.myhealthapp.usecase;

public class LoginMemberException extends RuntimeException {
    public LoginMemberException(String message) {
        super(message);
    }

    public LoginMemberException(String message, Throwable cause) {
        super(message, cause);
    }
}
