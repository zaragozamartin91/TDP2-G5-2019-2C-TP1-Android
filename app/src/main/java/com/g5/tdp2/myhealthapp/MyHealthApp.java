package com.g5.tdp2.myhealthapp;

import android.app.Application;

import com.android.volley.RequestQueue;
import com.g5.tdp2.myhealthapp.service.WebLoginMember;
import com.g5.tdp2.myhealthapp.service.WebSignupMember;
import com.g5.tdp2.myhealthapp.usecase.LoginMember;
import com.g5.tdp2.myhealthapp.usecase.SignupMember;

public class MyHealthApp extends Application {
    private RequestQueue requestQueue;

    @Override
    public void onCreate() {
        super.onCreate();

//        CrmBeanFactory.INSTANCE.addBean(new LoginMemberStub());

//        requestQueue = Volley.newRequestQueue(getApplicationContext());
//
//        CrmBeanFactory.INSTANCE.addBean(loginMember());
//        CrmBeanFactory.INSTANCE.addBean(signupMember());
    }

    private LoginMember loginMember() {
        WebLoginMember webLoginMember = new WebLoginMember(
                "https://tdp2-crmedical-api.herokuapp.com/auth/login",
                requestQueue);
        return webLoginMember;
    }

    private SignupMember signupMember() {
        return new WebSignupMember(
                "https://tdp2-crmedical-api.herokuapp.com/auth/register",
                requestQueue
        );
    }
}
