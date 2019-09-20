package com.g5.tdp2.myhealthapp.usecase;

import com.g5.tdp2.myhealthapp.entity.MemberSignupForm;

import java.util.function.Consumer;

public interface SignupMember {
    String INTERNAL_ERROR = "INTERNAL_ERROR";
    String INVALID_FORM = "INVALID_FORM";
    String UNKNOWN_ERROR = "UNKNOWN_ERROR";


    void signup(MemberSignupForm signupForm, Runnable succCallback, Consumer<Exception> errCallback);
}
