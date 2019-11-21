package com.g5.tdp2.myhealthapp.entity;

import java.util.function.Function;

import static org.apache.commons.lang3.StringUtils.isBlank;

public class SanatoriumSearchForm extends ProviderSearchForm {

    public SanatoriumSearchForm(Specialty specialty, Zone zone, String name, String plan) {
        super(specialty, zone, name, plan);
    }

    /**
     * Concatena valores para armar un query string de un sanatorio.
     *
     * @param base    String base (ej: una url)
     * @param valSep  Separador de valor (ej: '=')
     * @param itemSep Separador de items (ej: '&')
     * @param encode  Funcion de encoding (ej: {@link java.net.URLEncoder#encode(String, String)}
     * @return Query string
     */
    public String concat(String base, String valSep, String itemSep, Function<String, String> encode) {
        return base + "type" + valSep + "SANATORIO" + // type
                itemSep + "plan" + valSep + plan + // plan
                (Specialty.DEFAULT_SPECIALTY.equals(specialty) ? "" : itemSep + "specialty" + valSep + encode.apply("" + specialty.getId())) + // especialidad
                (Zone.DEFAULT_ZONE.equals(zone) ? "" : itemSep + "zone" + valSep + encode.apply("" + zone.getId())) + // zona
                (isBlank(name) ? "" : itemSep + "name" + valSep + encode.apply(name)); // nombre
    }

    @Override
    public String toString() {
        return "SanatoriumSearchForm{" +
                "specialty='" + specialty + '\'' +
                ", zone='" + zone + '\'' +
                ", name='" + name + '\'' +
                ", plan='" + plan + '\'' +
                '}';
    }
}

