package com.g5.tdp2.myhealthapp.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Specialty extends SimpleEntity {
    public static final Specialty DEFAULT_SPECIALTY = new Specialty(0, "Seleccione una especialidad");

    @JsonCreator
    public Specialty(@JsonProperty("id") long id, @JsonProperty("name") String name) {
        super(id, name);
    }

    @Override
    public String toString() {
        return getName();
    }
}
