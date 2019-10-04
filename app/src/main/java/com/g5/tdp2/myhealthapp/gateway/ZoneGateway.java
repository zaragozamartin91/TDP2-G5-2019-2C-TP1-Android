package com.g5.tdp2.myhealthapp.gateway;

import java.util.List;
import java.util.function.Consumer;

public interface ZoneGateway extends Gateway {
    void getZones(Consumer<List<String>> succCallback, Consumer<Exception> errCallback);
}
