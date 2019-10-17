package com.g5.tdp2.myhealthapp.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

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

    @JsonIgnore
    public List<Professional> flattenByOffice() {
        return getOffices().stream().map(of -> new Professional(
                getName(),
                getLanguages(),
                getSpecialties(),
                Collections.singletonList(of),
                getPlan(),
                getEmails())).collect(Collectors.toList());
    }
}
