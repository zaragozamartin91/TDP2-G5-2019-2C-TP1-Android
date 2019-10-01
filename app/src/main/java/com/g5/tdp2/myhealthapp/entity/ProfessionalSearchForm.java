package com.g5.tdp2.myhealthapp.entity;

import org.apache.commons.lang3.Validate;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

public class ProfessionalSearchForm {
    public static final String ALL_BLANK_FIELDS = "ALL_BLANK_FIELDS";
    public static final String INVALID_PLAN = "INVALID_PLAN";

    private String specialty;
    private String zone;
    private String name;
    private String plan;

    public ProfessionalSearchForm(String specialty, String zone, String name, String plan) {
        this.specialty = specialty;
        this.zone = zone;
        this.name = name;
        this.plan = plan;
    }

    public void setSpecialty(String specialty) {
        this.specialty = specialty;
    }

    public void setZone(String zone) {
        this.zone = zone;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPlan(String plan) {
        this.plan = plan;
    }

    public String getSpecialty() {
        return specialty;
    }

    public String getZone() {
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
                isNotBlank(specialty) || isNotBlank(zone) || isNotBlank(name),
                ALL_BLANK_FIELDS
        );
        Validate.validState(isNotBlank(plan), INVALID_PLAN);
    }

    @Override
    public String toString() {
        return "ProfessionalSearchForm{" +
                "specialty='" + specialty + '\'' +
                ", zone='" + zone + '\'' +
                ", name='" + name + '\'' +
                ", plan='" + plan + '\'' +
                '}';
    }
}

/* * Busqueda de prestadores/profesionales:
 * En buscador:
 * por especialidad
 * por zona (barrio en CABA)
 * por nombre
 */