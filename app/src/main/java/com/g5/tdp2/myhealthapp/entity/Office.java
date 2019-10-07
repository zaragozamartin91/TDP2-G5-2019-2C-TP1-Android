package com.g5.tdp2.myhealthapp.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.Optional;

public class Office implements Serializable {
    private String address;
    private String phone;
    private double lat;
    private double lon;
    private String zone;

    @JsonCreator
    public Office(
            @JsonProperty("address") String address,
            @JsonProperty("phone") String phone,
            @JsonProperty("lat") double lat,
            @JsonProperty("lon") double lon,
            @JsonProperty("zone") String zone) {
        this.address = address;
        this.phone = phone;
        this.lat = lat;
        this.lon = lon;
        this.zone = zone;
    }

    public String getAddress() {
        return address;
    }

    public String getPhone() {
        return phone;
    }

    public double getLat() {
        return lat;
    }

    public double getLon() {
        return lon;
    }

    public String getZone() {
        return zone;
    }

    public String addressWphone() {
        String fullAddress = address + ", " + zone;
        return Optional.ofNullable(phone).filter(p -> !p.isEmpty()).map(p -> fullAddress + " - " + p).orElse(fullAddress);
    }
}
