package com.g5.tdp2.myhealthapp;

import android.app.Application;

import com.g5.tdp2.myhealthapp.service.LoginMemberStub;
import com.g5.tdp2.myhealthapp.service.SignupMemberStub;
import com.g5.tdp2.myhealthapp.usecase.UsecaseFactory;

public class MyHealthApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        UsecaseFactory.INSTANCE.addBean(new LoginMemberStub());
        UsecaseFactory.INSTANCE.addBean(new SignupMemberStub());
    }
}
