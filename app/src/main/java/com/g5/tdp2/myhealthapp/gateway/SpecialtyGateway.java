package com.g5.tdp2.myhealthapp.gateway;

import com.g5.tdp2.myhealthapp.entity.Specialty;

import java.util.List;
import java.util.function.Consumer;

public interface SpecialtyGateway extends Gateway {
    void getSpecialties(Consumer<List<Specialty>> succCallback, Consumer<Exception> errCallback);
}
