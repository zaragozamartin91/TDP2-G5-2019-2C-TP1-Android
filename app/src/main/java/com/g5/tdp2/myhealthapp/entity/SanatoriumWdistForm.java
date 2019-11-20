package com.g5.tdp2.myhealthapp.entity;

import java.util.function.Function;

public class SanatoriumWdistForm extends ProviderWdistForm {

    public SanatoriumWdistForm(Specialty specialty, String plan, double distance, Place myPlace) {
        super(specialty, plan, distance, myPlace);
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
                (Specialty.DEFAULT_SPECIALTY.equals(specialty) ? "" : itemSep + "specialty" + valSep + encode.apply("" + specialty.getId()));  // especialidad
    }

    @Override
    public String toString() {
        return "SanatoriumWdistForm{" +
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