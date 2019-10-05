package com.g5.tdp2.myhealthapp.gateway;

import com.g5.tdp2.myhealthapp.entity.Zone;

import java.util.List;
import java.util.function.Consumer;

public interface ZoneGateway extends Gateway {
    void getZones(Consumer<List<Zone>> succCallback, Consumer<Exception> errCallback);
}
