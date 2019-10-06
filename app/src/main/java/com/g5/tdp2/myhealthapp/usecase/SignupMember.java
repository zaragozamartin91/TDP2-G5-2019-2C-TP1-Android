package com.g5.tdp2.myhealthapp.usecase;

import com.g5.tdp2.myhealthapp.entity.MemberSignupForm;

import java.util.function.Consumer;

/**
 * Caso de uso de registro de usuario
 */
public interface SignupMember {
    void signup(MemberSignupForm signupForm, Runnable succCallback, Consumer<Exception> errCallback);
}
