package com.g5.tdp2.myhealthapp.service;

import com.g5.tdp2.myhealthapp.entity.Office;
import com.g5.tdp2.myhealthapp.entity.Professional;
import com.g5.tdp2.myhealthapp.entity.ProfessionalSearchForm;
import com.g5.tdp2.myhealthapp.usecase.SearchProfessionals;
import com.g5.tdp2.myhealthapp.usecase.SearchProfessionalsException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

public class SearchProfessionalsStub implements SearchProfessionals {

    @Override
    public void searchProfessionals(
            ProfessionalSearchForm form,
            Consumer<List<Professional>> succCallback,
            Consumer<Exception> errCallback) throws SearchProfessionalsException {
        try {
            form.validate();
        } catch (IllegalStateException e) {
            errCallback.accept(new SearchProfessionalsException(INVALID_FORM, e));
        }

        List<Professional> professionals = new ArrayList<>();

        {
            String name = "Pepe Argento";
            List<String> languages = Arrays.asList("Ingles", "Espanol");
            List<String> specialties = Collections.singletonList("Oncologia");
            List<Office> offices = Collections.singletonList(new Office("Av falsa 123", "47238511", 35.2d, 33.3d));
            List<String> plans = Arrays.asList("A110", "A210");
            List<String> emails = Collections.singletonList("pepe@argento.com");
            professionals.add(new Professional(name, languages, specialties, offices, plans, emails));
        }

        {
            String name = "Jotaro Kujo";
            List<String> languages = Arrays.asList("Japones", "Ingles");
            List<String> specialties = Arrays.asList("Clinica", "Pediatria");
            List<Office> offices = Collections.singletonList(new Office("Av bar 456", "", 36.2d, 34.3d));
            List<String> plans = Arrays.asList("A210", "A310");
            List<String> emails = Collections.singletonList("jotaro@kujo.com");
            professionals.add(new Professional(name, languages, specialties, offices, plans, emails));
        }

        succCallback.accept(professionals);
    }
}
