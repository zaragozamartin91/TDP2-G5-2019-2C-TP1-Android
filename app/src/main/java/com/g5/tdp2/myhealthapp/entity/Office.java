package com.g5.tdp2.myhealthapp.entity;

import java.io.Serializable;
import java.util.Optional;

public class Office implements Serializable {
    private String address;
    private String phone;
    private double lat;
    private double lon;

    public Office(String address, String phone, double lat, double lon) {
        this.address = address;
        this.phone = phone;
        this.lat = lat;
        this.lon = lon;
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

    public String addressWphone() {
        return Optional.ofNullable(phone).filter(p -> !p.isEmpty()).map(p -> address + " - " + p).orElse(address);
    }
}
