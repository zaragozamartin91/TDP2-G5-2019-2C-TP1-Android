package com.g5.tdp2.myhealthapp;


import com.g5.tdp2.myhealthapp.entity.Checktype;
import com.g5.tdp2.myhealthapp.entity.Specialty;
import com.g5.tdp2.myhealthapp.entity.Zone;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Representa el estado INMUTABLE de la aplicacion (guarda catalogos de zonas, especialidades, etc.)
 */
public enum AppState {
    INSTANCE;

    public static final String ZONES_KEY = "ZONES";
    public static final String SPECIALTIES_KEY = "SPECIALTIES";
    public static final String CHECKTYPES_KEY = "AUTHTYPES";

    private Map<String, Object> state = new ConcurrentHashMap<>();

    public Object put(String key, Object value) {
        return state.put(key, value);
    }

    public Object get(String key) {
        return state.get(key);
    }

    /**
     * Obtiene el catalogo de localidades
     *
     * @return catalogo de localidades
     */
    public List<Zone> getZones() {
        return (List<Zone>) getOrDefault(ZONES_KEY, Collections.emptyList());
    }

    /**
     * Obtiene el catalogo de especialidades
     *
     * @return catalogo de especialidades
     */
    public List<Specialty> getSpecialties() { return (List<Specialty>) getOrDefault(SPECIALTIES_KEY, Collections.emptyList()); }

    /**
     * Obtiene el catalogo de tipos de estudio
     *
     * @return catalogo de tipos de estudio
     */
    public List<Checktype> getChecktypes() { return (List<Checktype>) getOrDefault(CHECKTYPES_KEY, Collections.emptyList()); }

    /**
     * Obtiene una especialidad a partir de su id
     *
     * @param specId Id de especialidad
     * @return Especialidad con el id solicitado
     */
    public Specialty getSpecialty(long specId) {
        return getSpecialties().stream()
                .filter(s -> specId == s.getId())
                .findFirst()
                .orElse(Specialty.DEFAULT_SPECIALTY);
    }

    public Object getOrDefault(String key, Object defaultValue) {
        return state.getOrDefault(key, defaultValue);
    }
}
