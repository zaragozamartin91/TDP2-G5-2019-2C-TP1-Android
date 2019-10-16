package com.g5.tdp2.myhealthapp.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.Optional;
import java.util.regex.Pattern;

public class Office implements Serializable, Place {
    private static final String ZONE_DELIM = ", ";
    private static final String PHONE_DELIM = " - ";
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

    @Override
    public double getLat() {
        return lat;
    }

    @Override
    public double getLon() {
        return lon;
    }

    public String getZone() {
        return zone;
    }

    public String addressWphone() {
        String fullAddress = address + ZONE_DELIM + zone;
        return Optional.ofNullable(phone)
                .filter(p -> !p.isEmpty())
                .map(p -> fullAddress + PHONE_DELIM + p)
                .orElse(fullAddress);
    }

    public static Office unzip(String addrWphone) {
        if (addrWphone.contains(PHONE_DELIM)) {
            String[] split = addrWphone.split(Pattern.quote(PHONE_DELIM));
            String addrWzone = split[0];
            String phone = split[1];
            String[] addrWzoneSplit = addrWzone.split(ZONE_DELIM);
            String address = addrWzoneSplit[0];
            String zone = addrWzoneSplit[1];
            return new Office(address, phone, 0, 0, zone);
        } else {
            String[] addrWzoneSplit = addrWphone.split(ZONE_DELIM);
            String address = addrWzoneSplit[0];
            String zone = addrWzoneSplit[1];
            return new Office(address, "", 0, 0, zone);
        }
    }
}
