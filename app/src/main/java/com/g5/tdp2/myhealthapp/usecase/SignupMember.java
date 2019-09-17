package com.g5.tdp2.myhealthapp.usecase;

import com.g5.tdp2.myhealthapp.entity.MemberSignupForm;

public interface SignupMember {
    void signup(MemberSignupForm signupForm) throws IllegalStateException, SignupMemberException;
}
