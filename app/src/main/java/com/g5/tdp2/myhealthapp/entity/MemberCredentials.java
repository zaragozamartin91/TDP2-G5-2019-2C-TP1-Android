package com.g5.tdp2.myhealthapp.entity;


import com.fasterxml.jackson.annotation.JsonProperty;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;

public class MemberCredentials {
    public static final String INVALID_ID = "INVALID_ID";
    public static final String EMPTY_PASSWORD = "EMPTY_PASSWORD";

    private long id;
    private String password;
    private String fbToken; // token de firebase

    public MemberCredentials(long id, String password, String fbToken) {
        this.id = id;
        this.password = password;
        this.fbToken = fbToken;
    }

    public static MemberCredentials of(String sid, String password) throws NumberFormatException {
        return of(sid, password, "");
    }

    public static MemberCredentials of(String sid, String password, String fbToken) throws NumberFormatException {
        return new MemberCredentials(Long.valueOf(sid), password, fbToken);
    }

    /**
     * Valida las credenciales del usuario.
     *
     * @throws IllegalStateException En caso que los campos sean erroneos
     */
    public void validate() throws IllegalStateException {
        Validate.validState(id > 0L, INVALID_ID);
        Validate.validState(StringUtils.isNotBlank(password), EMPTY_PASSWORD);
    }

    @JsonProperty("idn")
    public long getId() {
        return id;
    }

    @JsonProperty("password")
    public String getPassword() {
        return password;
    }

    @JsonProperty("role")
    public String getRole() { return "affiliate"; }

    @JsonProperty("device_token")
    public String getFbToken() { return fbToken; }
}
