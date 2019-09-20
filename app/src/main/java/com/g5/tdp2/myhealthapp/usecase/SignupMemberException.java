package com.g5.tdp2.myhealthapp.usecase;

public class SignupMemberException extends RuntimeException {
    public SignupMemberException(String message) {
        super(message);
    }

    public SignupMemberException(String message, Throwable cause) {
        super(message, cause);
    }
}
