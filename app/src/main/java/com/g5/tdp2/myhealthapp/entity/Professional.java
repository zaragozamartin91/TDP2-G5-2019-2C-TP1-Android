package com.g5.tdp2.myhealthapp.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Entidad profesional
 */
public class Professional extends Provider {
    @JsonCreator
    public Professional(
            @JsonProperty("name") String name,
            @JsonProperty("languages") List<String> languages,
            @JsonProperty("specialties") List<String> specialties,
            @JsonProperty("offices") List<Office> offices,
            @JsonProperty("plan") String plan,
            @JsonProperty("emails") List<String> emails) {
        super(name, languages, specialties, offices, plan, emails);
    }
}
