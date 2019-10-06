package com.g5.tdp2.myhealthapp.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Zone extends SimpleEntity {
    public static final Zone DEFAULT_ZONE = new Zone(0, "Seleccione una zona");

    @JsonCreator
    public Zone(@JsonProperty("id") long id, @JsonProperty("name") String name) {
        super(id, name);
    }

    @Override
    public String toString() {
        return getName();
    }
}
