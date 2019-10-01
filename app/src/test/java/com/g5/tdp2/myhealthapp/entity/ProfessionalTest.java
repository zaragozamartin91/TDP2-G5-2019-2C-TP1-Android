package com.g5.tdp2.myhealthapp.entity;

import com.g5.tdp2.myhealthapp.util.JsonParser;

import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertNotNull;

public class ProfessionalTest {
    @Test
    public void asJson() {
        {
            String name = "Pepe Argento";
            List<String> languages = Arrays.asList("Ingles", "Espanol");
            List<String> specialties = Collections.singletonList("Oncologia");
            List<Office> offices = Arrays.asList(
                    new Office("Av falsa 123", "47238511", -34.5956d, -58.4406d),
                    new Office("Av moni 456", "", -34.5999d, -58.4507d)
            );
            String plan = "A110";
            List<String> emails = Collections.singletonList("pepe@argento.com");
            Professional professional = new Professional(name, languages, specialties, offices, plan, emails);

            String jp = JsonParser.INSTANCE.writeValueAsString(professional);
            assertNotNull(jp);
            System.out.println(jp);
        }
    }
}