package com.g5.tdp2.myhealthapp.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Checktype extends SimpleEntity {
    public static final Checktype DEFAULT_CHECKTYPE = new Checktype(0, "Seleccione un tipo de estudio");

    @JsonCreator
    public Checktype(@JsonProperty("id") long id, @JsonProperty("name") String name) {
        super(id, name);
    }

    @Override
    public String toString() {
        return getName();
    }
}
