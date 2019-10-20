package com.g5.tdp2.myhealthapp.service;

import com.g5.tdp2.myhealthapp.entity.NewCheckForm;
import com.g5.tdp2.myhealthapp.usecase.PostNewCheck;

import java.util.function.Consumer;

public class WebPostNewCheck implements PostNewCheck {
    @Override
    public void postNewCheck(NewCheckForm newCheckForm, Runnable succCallback, Consumer<Exception> errCallback) {

    }
}
