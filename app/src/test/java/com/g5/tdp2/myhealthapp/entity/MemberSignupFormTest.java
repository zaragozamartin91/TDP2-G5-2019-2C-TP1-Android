package com.g5.tdp2.myhealthapp.entity;

import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.*;

public class MemberSignupFormTest {

    @Test
    public void validateEmptyFirstName() {
        MemberSignupForm memberSignupForm = new MemberSignupForm("", "lastName", "email@gmail.com",
                15543L, "memberId", "A310", new Date(), "password", "password");
        try {
            memberSignupForm.validate();
        } catch (IllegalStateException e) {
            assertEquals(e.getMessage(), MemberSignupForm.EMPTY_FIRST_NAME);
        }
    }

    @Test
    public void validateEmptyLastName() {
        MemberSignupForm memberSignupForm = new MemberSignupForm("firstName", "", "email@gmail.com",
                15543L, "memberId", "A310", new Date(), "password", "password");
        try {
            memberSignupForm.validate();
        } catch (IllegalStateException e) {
            assertEquals(e.getMessage(), MemberSignupForm.EMPTY_LAST_NAME);
        }
    }

    @Test
    public void validateInvalidPassword() {
        MemberSignupForm memberSignupForm = new MemberSignupForm("firstName", "lastName", "email@gmail.com",
                15543L, "memberId", "A310", new Date(), "", "password");
        try {
            memberSignupForm.validate();
        } catch (IllegalStateException e) {
            assertEquals(e.getMessage(), MemberSignupForm.INVALID_PASSWORD);
        }
    }

    @Test
    public void validateInvalidId() {
        MemberSignupForm memberSignupForm = new MemberSignupForm("firstName", "lastName", "email@gmail.com",
                0L, "memberId", "A310", new Date(), "password", "password");
        try {
            memberSignupForm.validate();
        } catch (IllegalStateException e) {
            assertEquals(e.getMessage(), MemberSignupForm.INVALID_ID);
        }
    }

    @Test
    public void validateInvalidMemberId() {
        MemberSignupForm memberSignupForm = new MemberSignupForm("firstName", "lastName", "email@gmail.com",
                15543L, "", "A310", new Date(), "password", "password");
        try {
            memberSignupForm.validate();
        } catch (IllegalStateException e) {
            assertEquals(e.getMessage(), MemberSignupForm.INVALID_MEMBER_ID);
        }
    }

    @Test
    public void validatePasswordsDontMatch() {
        MemberSignupForm memberSignupForm = new MemberSignupForm("firstName", "lastName", "email@gmail.com",
                15543L, "memberId", "A310", new Date(), "casa1236564ggrdf", "password");
        try {
            memberSignupForm.validate();
        } catch (IllegalStateException e) {
            assertEquals(e.getMessage(), MemberSignupForm.PASSWORDS_DONT_MATCH);
        }
    }

    @Test
    public void validateInvalidPlan() {
        MemberSignupForm memberSignupForm = new MemberSignupForm("firstName", "lastName", "email@gmail.com",
                15543L, "memberId", "", new Date(), "password1234", "password1234");
        try {
            memberSignupForm.validate();
        } catch (IllegalStateException e) {
            assertEquals(e.getMessage(), MemberSignupForm.INVALID_PLAN);
        }
    }

    @Test
    public void validate() {
        String firstName = "firstName";
        String lastName = "lastName";
        String email = "email@gmail.com";
        long id = 15543L;
        String memberId = "memberId";
        String plan = "A310";
        Date date = new Date();
        String password = "password1234";
        MemberSignupForm memberSignupForm = new MemberSignupForm(firstName, lastName, email,
                id, memberId, plan, date, password, password);
        memberSignupForm.validate();
        assertEquals(memberSignupForm.getBirth(), date);
        assertEquals(memberSignupForm.getEmail(), email);
        assertEquals(memberSignupForm.getFirstName(), firstName);
        assertEquals(memberSignupForm.getLastName(), lastName);
        assertEquals(memberSignupForm.getId(), id);
        assertEquals(memberSignupForm.getMemberId(), memberId);
        assertEquals(memberSignupForm.getPassword(), password);
        assertEquals(memberSignupForm.getRepPassword(), password);
    }
}