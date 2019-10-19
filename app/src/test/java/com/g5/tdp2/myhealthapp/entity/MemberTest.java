package com.g5.tdp2.myhealthapp.entity;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.g5.tdp2.myhealthapp.util.DateFormatter;

import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;

public class MemberTest {
    @Test
    public void fromJson() throws IOException {
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
        assertEquals("Christian", member.getFirstname());
        assertEquals("Angelone", member.getLastname());
        assertEquals("1990-08-31", DateFormatter.YYYY_MM_DD.serialize(member.getBirthdate()));
        assertEquals("0987654321", member.getMemberId());
        assertEquals("A310", member.getPlan());
        assertEquals(34317677L, member.getIdn());
        assertEquals("zaragozamartin91@gmail.com", member.getEmail());
        System.out.println(member);

        String jsonMember = new ObjectMapper().writeValueAsString(member);
        System.out.println(jsonMember);
        assertTrue(jsonMember.contains("\"1990-08-31\""));
    }
}