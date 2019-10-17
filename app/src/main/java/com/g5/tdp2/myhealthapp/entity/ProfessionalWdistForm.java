package com.g5.tdp2.myhealthapp.entity;

import org.apache.commons.lang3.Validate;

import java.util.Objects;
import java.util.function.Function;

import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

public class ProfessionalWdistForm {
    public static final String ALL_BLANK_FIELDS = "ALL_BLANK_FIELDS";
    public static final String INVALID_PLAN = "INVALID_PLAN";

    private Specialty specialty;
    private String plan;
    private double distance;
    private Place myPlace;

    public ProfessionalWdistForm(Specialty specialty, String plan, double distance, Place myPlace) {
        this.specialty = specialty;
        this.plan = plan;
        this.distance = distance;
        this.myPlace = myPlace;
    }

    public Specialty getSpecialty() { return specialty; }

    public String getPlan() { return plan; }

    /**
     * Maxima distancia en metros de la oficina del profesional solicitado
     *
     * @return Maxima distancia en metros de la oficina del profesional solicitado
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

    /**
     * Concatena valores para armar un query string de un profesional.
     *
     * @param base    String base (ej: una url)
     * @param valSep  Separador de valor (ej: '=')
     * @param itemSep Separador de items (ej: '&')
     * @param encode  Funcion de encoding (ej: {@link java.net.URLEncoder#encode(String, String)}
     * @return Query string
     */
    public String concat(String base, String valSep, String itemSep, Function<String, String> encode) {
        return base + "type" + valSep + "PROFESIONAL" + // type
                itemSep + "plan" + valSep + plan + // plan
                (Specialty.DEFAULT_SPECIALTY.equals(specialty) ? "" : itemSep + "specialty" + valSep + encode.apply("" + specialty.getId()));  // especialidad
    }

    @Override
    public String toString() {
        return "ProfessionalWdistForm{" +
                "specialty=" + specialty +
                ", plan='" + plan + '\'' +
                ", distance=" + distance +
                ", myPlace=" + myPlace +
                '}';
    }
}

/* * Busqueda de prestadores/profesionales:
 * En buscador:
 * por especialidad
 * por zona (barrio en CABA)
 * por nombre
 */