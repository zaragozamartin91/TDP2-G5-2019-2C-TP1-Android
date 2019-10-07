package com.g5.tdp2.myhealthapp;


import com.g5.tdp2.myhealthapp.entity.Specialty;
import com.g5.tdp2.myhealthapp.entity.Zone;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Representa el estado de la aplicacion
 */
public enum AppState {
    INSTANCE;

    public static final String TOKEN_KEY = "TOKEN";
    public static final String ZONES_KEY = "ZONES";
    public static final String SPECIALTIES_KEY = "SPECIALTIES";

    private Map<String, Object> state = new ConcurrentHashMap<>();

    public void putToken(String token) {
        this.put(TOKEN_KEY, token);
    }

    public Optional<String> getToken() {
        return Optional.ofNullable(get(TOKEN_KEY)).map(Object::toString);
    }

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

    public Object getOrDefault(String key, Object defaultValue) {
        return state.getOrDefault(key, defaultValue);
    }
}
