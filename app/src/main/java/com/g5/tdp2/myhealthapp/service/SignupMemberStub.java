package com.g5.tdp2.myhealthapp.service;

import com.g5.tdp2.myhealthapp.entity.MemberSignupForm;
import com.g5.tdp2.myhealthapp.usecase.SignupMember;
import com.g5.tdp2.myhealthapp.usecase.SignupMemberException;

public class SignupMemberStub implements SignupMember {
    @Override
    public void signup(MemberSignupForm signupForm) throws IllegalStateException, SignupMemberException {
        signupForm.validate();
    }
}
