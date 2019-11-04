package com.g5.tdp2.myhealthapp.gateway;

import com.g5.tdp2.myhealthapp.entity.Checktype;

import java.util.List;
import java.util.function.Consumer;

public interface ChecktypeGateway extends Gateway {
    void getChecktypes(Consumer<List<Checktype>> succCallback, Consumer<Exception> errCallback);
}
