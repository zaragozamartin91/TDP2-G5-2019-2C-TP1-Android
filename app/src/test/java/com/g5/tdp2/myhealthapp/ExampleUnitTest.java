package com.g5.tdp2.myhealthapp;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.g5.tdp2.myhealthapp.entity.MemberSignupForm;
import com.g5.tdp2.myhealthapp.service.LoginMemberStub;
import com.g5.tdp2.myhealthapp.usecase.LoginMember;

import org.junit.Test;

import java.util.Calendar;

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

    @Test
    public void jacksonTest() throws JsonProcessingException {
        MemberSignupForm memberSignupForm = new MemberSignupForm(
                "martin", "zaragoza", "zaragozamartin91@gmail.com", 123L, "asd123", "210A",
                Calendar.getInstance().getTime(), "asdasd123123", "asdasd123123"
        );

        System.out.println(new ObjectMapper().writeValueAsString(memberSignupForm));
    }
}