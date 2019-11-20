package com.g5.tdp2.myhealthapp.service;

import com.g5.tdp2.myhealthapp.entity.Office;
import com.g5.tdp2.myhealthapp.entity.Sanatorium;
import com.g5.tdp2.myhealthapp.entity.SanatoriumWdistForm;
import com.g5.tdp2.myhealthapp.usecase.SearchProvidersException;
import com.g5.tdp2.myhealthapp.usecase.SearchSanatoriums;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

public class SearchSanatoriumsStub implements SearchSanatoriums {


    @Override
    public void searchSanatoriums(SanatoriumWdistForm form, Consumer<List<Sanatorium>> succCallback, Consumer<Exception> errCallback) throws SearchProvidersException {
        try {
            form.validate();
        } catch (IllegalStateException e) {
            errCallback.accept(new SearchProvidersException(INVALID_FORM, e));
            return;
        }

        getSans(succCallback);
    }

    private void getSans(Consumer<List<Sanatorium>> succCallback) {
        List<Sanatorium> sanatoriums = new ArrayList<>();

        {
            String name = "Hospital Italiano de Buenos Aires";
            List<String> languages = Arrays.asList("Ingles", "Espanol");
            List<String> specialties = Arrays.asList("CIRUGÍA DE MANO","CIRUGÍA DENTO-MAXILAR","CIRUGÍA GENERAL");
            List<Office> offices = Collections.singletonList(new Office("Av falsa 987", "47238511", 35.2d, 33.3d, "Villa crespo"));
            String plan = "A110";
            List<String> emails = Collections.singletonList("sanatorio@italiano.com");
            sanatoriums.add(new Sanatorium(name, languages, specialties, offices, plan, emails));
        }

        {
            String name = "Plaza de Mayo";
            List<String> languages = Arrays.asList("Ingles", "Espanol");
            List<String> specialties = Collections.singletonList("ESTOMATOLOGÍA ADULTOS");
            List<Office> offices = Collections.singletonList(new Office("Av bar 546", "", 36.2d, 34.3d, "Villa crespo"));
            String plan = "A310";
            List<String> emails = Collections.singletonList("plaza@mayo.com");
            sanatoriums.add(new Sanatorium(name, languages, specialties, offices, plan, emails));
        }

        succCallback.accept(sanatoriums);
    }
}
