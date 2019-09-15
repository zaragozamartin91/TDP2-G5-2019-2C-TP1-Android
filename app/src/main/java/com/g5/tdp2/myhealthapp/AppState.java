package com.g5.tdp2.myhealthapp;


import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Representa el estado de la aplicacion
 */
public enum AppState {
    INSTANCE;

    public static final String TOKEN_KEY = "TOKEN";

    private Map<String, Object> state = new HashMap<>();

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

    public Object getOrDefault(String key, Object defaultValue) {
        return state.getOrDefault(key, defaultValue);
    }
}
