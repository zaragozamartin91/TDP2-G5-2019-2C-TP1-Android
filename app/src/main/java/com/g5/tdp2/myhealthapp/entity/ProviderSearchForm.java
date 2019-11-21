package com.g5.tdp2.myhealthapp.entity;

import org.apache.commons.lang3.Validate;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

class ProviderSearchForm {
    public static final String ALL_BLANK_FIELDS = "ALL_BLANK_FIELDS";
    public static final String INVALID_PLAN = "INVALID_PLAN";
    protected Specialty specialty;
    protected Zone zone;
    protected String name;
    protected String plan;

    ProviderSearchForm(Specialty specialty, Zone zone, String name, String plan) {
        this.specialty = specialty;
        this.zone = zone;
        this.name = name;
        this.plan = plan;
    }

    public Specialty getSpecialty() {
        return specialty;
    }

    public Zone getZone() {
        return zone;
    }

    public String getName() {
        return name;
    }

    public String getPlan() {
        return plan;
    }

    public void validate() throws IllegalStateException {
        Validate.validState(
                !Specialty.DEFAULT_SPECIALTY.equals(specialty)
                        || !Zone.DEFAULT_ZONE.equals(zone)
                        || isNotBlank(name),
                ALL_BLANK_FIELDS
        );
        Validate.validState(isNotBlank(plan), INVALID_PLAN);
    }
}
