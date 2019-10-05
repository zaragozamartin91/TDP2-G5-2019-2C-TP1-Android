package com.g5.tdp2.myhealthapp;


import com.g5.tdp2.myhealthapp.entity.Zone;

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
     * Obtiene el catalogo de zonas
     *
     * @return catalogo de zonas
     */
    public List<Zone> getZones() {
        return (List<Zone>) get(ZONES_KEY);
    }

    public Object getOrDefault(String key, Object defaultValue) {
        return state.getOrDefault(key, defaultValue);
    }
}
