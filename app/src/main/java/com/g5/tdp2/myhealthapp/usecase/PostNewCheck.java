package com.g5.tdp2.myhealthapp.usecase;

import com.g5.tdp2.myhealthapp.entity.NewCheckForm;

import java.util.function.Consumer;

public interface PostNewCheck {
    void postNewCheck(NewCheckForm newCheckForm, Runnable succCallback, Consumer<Exception> errCallback);
}
