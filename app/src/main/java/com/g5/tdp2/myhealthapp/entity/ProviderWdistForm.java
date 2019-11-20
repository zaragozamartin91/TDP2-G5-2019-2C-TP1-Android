package com.g5.tdp2.myhealthapp.entity;

import org.apache.commons.lang3.Validate;

import java.util.Objects;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

class ProviderWdistForm {
    public static final String ALL_BLANK_FIELDS = "ALL_BLANK_FIELDS";
    public static final String INVALID_PLAN = "INVALID_PLAN";
    protected Specialty specialty;
    protected String plan;
    protected double distance;
    protected Place myPlace;

    ProviderWdistForm(Specialty specialty, String plan, double distance, Place myPlace) {
        this.specialty = specialty;
        this.plan = plan;
        this.distance = distance;
        this.myPlace = myPlace;
    }

    public Specialty getSpecialty() { return specialty; }

    public String getPlan() { return plan; }

    /**
     * Maxima distancia en metros de la oficina del proveedor solicitado
     *
     * @return Maxima distancia en metros de la oficina del proveedor solicitado
     */
    public double getDistance() { return distance; }

    /**
     * Obtiene la ubicacion del usuario
     *
     * @return ubicacion del usuario
     */
    public Place getMyPlace() { return myPlace; }

    public void validate() throws IllegalStateException {
        Validate.validState(
                !Specialty.DEFAULT_SPECIALTY.equals(specialty)
                        && distance > 0d
                        && Objects.nonNull(myPlace),
                ALL_BLANK_FIELDS
        );
        Validate.validState(isNotBlank(plan), INVALID_PLAN);
    }
}
