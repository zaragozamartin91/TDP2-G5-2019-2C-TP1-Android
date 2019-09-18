package com.g5.tdp2.myhealthapp;

import android.app.Application;

import com.g5.tdp2.myhealthapp.service.SignupMemberStub;
import com.g5.tdp2.myhealthapp.service.WebLoginMember;
import com.g5.tdp2.myhealthapp.usecase.LoginMember;
import com.g5.tdp2.myhealthapp.usecase.SignupMember;
import com.g5.tdp2.myhealthapp.usecase.UsecaseFactory;

public class MyHealthApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

//        UsecaseFactory.INSTANCE.addBean(new LoginMemberStub());

        UsecaseFactory.INSTANCE.addBean(loginMember());
        UsecaseFactory.INSTANCE.addBean(signupMember());
    }

    private LoginMember loginMember() {
        return new WebLoginMember(
                "https://tdp2-crmedical-api.herokuapp.com/auth/login",
                json -> AppState.INSTANCE.putToken(json.token));
    }

    private SignupMember signupMember() {
        return new SignupMemberStub();
    }
}
