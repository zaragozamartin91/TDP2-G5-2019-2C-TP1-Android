package com.g5.tdp2.myhealthapp.entity;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;

public class MemberTest {
    @Test public void fromJson() throws IOException {
        String json = "{\n" +
                "        \"firstname\": \"Christian\",\n" +
                "        \"lastname\": \"Angelone\",\n" +
                "        \"birthdate\": \"1990-08-31\",\n" +
                "        \"affiliate_id\": \"0987654321\",\n" +
                "        \"plan\": \"A310\",\n" +
                "        \"idn\": 34317677,\n" +
                "        \"email\": \"zaragozamartin91@gmail.com\"\n" +
                "    }";

        Member member = new ObjectMapper().readValue(json, Member.class);
        System.out.println(member);
    }
}