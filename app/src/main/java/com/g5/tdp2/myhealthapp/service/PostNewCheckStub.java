package com.g5.tdp2.myhealthapp.service;

import com.g5.tdp2.myhealthapp.entity.NewCheckForm;
import com.g5.tdp2.myhealthapp.usecase.PostNewCheck;
import com.g5.tdp2.myhealthapp.usecase.PostNewCheckException;

import java.util.function.Consumer;

public class PostNewCheckStub implements PostNewCheck {
    @Override
    public void postNewCheck(NewCheckForm newCheckForm, Runnable succCallback, Consumer<Exception> errCallback) {
        try {
            newCheckForm.validate();
            succCallback.run();
        } catch (Exception e) {
            errCallback.accept(new PostNewCheckException(e.getMessage(), e));
        }
    }
}
