package com.g5.tdp2.myhealthapp;

import com.g5.tdp2.myhealthapp.service.LoginMemberStub;
import com.g5.tdp2.myhealthapp.usecase.LoginMember;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void cast() {
        LoginMemberStub lms = new LoginMemberStub();
        LoginMember lm = lms;
        Class<LoginMember> clm = LoginMember.class;
        assertTrue(clm.isInstance(lm));
        assertTrue(clm.isInstance(lms));

    }
}