package com.g5.tdp2.myhealthapp.entity;

import org.apache.commons.lang3.Validate;

import java.util.function.Function;

import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

public class ProfessionalSearchForm {
    public static final String ALL_BLANK_FIELDS = "ALL_BLANK_FIELDS";
    public static final String INVALID_PLAN = "INVALID_PLAN";

    private Specialty specialty;
    private Zone zone;
    private String name;
    private String plan;

    public ProfessionalSearchForm(Specialty specialty, Zone zone, String name, String plan) {
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
                (Specialty.DEFAULT_SPECIALTY.equals(specialty) ? "" : itemSep + "specialty" + valSep + encode.apply("" + specialty.getId())) + // especialidad
                (Zone.DEFAULT_ZONE.equals(zone) ? "" : itemSep + "zone" + valSep + encode.apply("" + zone.getId())) + // zona
                (isBlank(name) ? "" : itemSep + "name" + valSep + encode.apply(name)); // nombre
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