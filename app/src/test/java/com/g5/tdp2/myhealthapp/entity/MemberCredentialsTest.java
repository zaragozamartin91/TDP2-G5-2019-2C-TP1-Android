package com.g5.tdp2.myhealthapp.entity;

import org.junit.Test;

import static org.junit.Assert.*;

public class MemberCredentialsTest {

    @Test
    public void of() {
        long sid = 123456L;
        String password = "qwerty123";
        MemberCredentials memberCredentials = MemberCredentials.of(String.valueOf(sid), password);
        assertEquals(memberCredentials.getId(), sid);
        assertEquals(memberCredentials.getPassword(), password);
    }

    @Test(expected = IllegalStateException.class)
    public void validateSid0() {
        long sid = 0L;
        String password = "qwerty123";
        MemberCredentials memberCredentials = MemberCredentials.of(String.valueOf(sid), password);
        memberCredentials.validate();
    }

    @Test(expected = IllegalStateException.class)
    public void validateEmptyPassword() {
        long sid = 15545242L;
        String password = "";
        MemberCredentials memberCredentials = MemberCredentials.of(String.valueOf(sid), password);
        memberCredentials.validate();
    }

    @Test
    public void validate() {
        long sid = 123456L;
        String password = "qwerty123";
        MemberCredentials memberCredentials = MemberCredentials.of(String.valueOf(sid), password);
        memberCredentials.validate();
    }
}