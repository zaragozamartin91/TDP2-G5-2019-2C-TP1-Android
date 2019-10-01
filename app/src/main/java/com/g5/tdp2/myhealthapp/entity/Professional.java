package com.g5.tdp2.myhealthapp.entity;

import java.io.Serializable;
import java.util.List;

public class Professional implements Serializable {
    private String name;
    private List<String> languages;
    private List<String> specialties;
    private List<Office> offices;
    private String plan;
    private List<String> emails;

    public Professional(String name, List<String> languages, List<String> specialties, List<Office> offices, String plan, List<String> emails) {
        this.name = name;
        this.languages = languages;
        this.specialties = specialties;
        this.offices = offices;
        this.plan = plan;
        this.emails = emails;
    }

    public String getName() {
        return name;
    }

    public List<String> getLanguages() {
        return languages;
    }

    public List<String> getSpecialties() {
        return specialties;
    }

    public List<Office> getOffices() {
        return offices;
    }

    public String getPlan() {
        return plan;
    }

    public List<String> getEmails() {
        return emails;
    }
}

/* Por lo que sabemos, un prestador va a tener: Nombre, Tipo(profesional, clinica, sanatorio),Idiomas, Especialidades, Direcciones, Plan, telefono, Emails
 * Un PRESTADOR puede tener UNA o mas direcciones (oficinas del prestador)
 * Cada direccion puede tener CERO o mas telefonos asociados (cada oficina puede tener o no un telefono)
 * Cada direccion debe tener valores de LATITUD y LONGITUD asociadas (Necesario para la busqueda de PRESTADORES cercanos del lado de Android usando el API de GoogleMaps)
 */