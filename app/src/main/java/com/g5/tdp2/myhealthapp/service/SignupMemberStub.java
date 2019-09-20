package com.g5.tdp2.myhealthapp.service;

import com.g5.tdp2.myhealthapp.entity.MemberSignupForm;
import com.g5.tdp2.myhealthapp.usecase.SignupMember;
import com.g5.tdp2.myhealthapp.usecase.SignupMemberException;

import java.util.function.Consumer;

public class SignupMemberStub implements SignupMember {
    @Override
    public void signup(MemberSignupForm signupForm, Runnable succCallback, Consumer<Exception> errCallback) {
        try {
            signupForm.validate();
            succCallback.run();
        } catch (Exception e) {
            errCallback.accept(e);
        }
    }
}
