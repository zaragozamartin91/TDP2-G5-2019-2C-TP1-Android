package com.g5.tdp2.myhealthapp.entity;


import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;

public class MemberCredentials {
    public static final String INVALID_ID = "INVALID_ID";
    public static final String EMPTY_PASSWORD = "EMPTY_PASSWORD";
    public static final String SHORT_PASSWORD = "SHORT_PASSWORD";

    private long id;
    private String password;

    public MemberCredentials(long id, String password) {
        this.id = id;
        this.password = password;
    }

    public static MemberCredentials of(String sid, String password) throws NumberFormatException {
        return new MemberCredentials(Long.valueOf(sid), password);
    }

    public void validate() {
        Validate.validState(id > 0L, INVALID_ID);
        Validate.validState(StringUtils.isNotBlank(password), EMPTY_PASSWORD);
        Validate.validState(password.length() > 8, SHORT_PASSWORD);
    }
}
