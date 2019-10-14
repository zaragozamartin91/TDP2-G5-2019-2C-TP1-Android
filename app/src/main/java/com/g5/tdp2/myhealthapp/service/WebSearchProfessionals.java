package com.g5.tdp2.myhealthapp.service;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.g5.tdp2.myhealthapp.entity.Place;
import com.g5.tdp2.myhealthapp.entity.Professional;
import com.g5.tdp2.myhealthapp.entity.ProfessionalSearchForm;
import com.g5.tdp2.myhealthapp.entity.ProfessionalWdistForm;
import com.g5.tdp2.myhealthapp.entity.Provider;
import com.g5.tdp2.myhealthapp.usecase.SearchProfessionals;
import com.g5.tdp2.myhealthapp.usecase.SearchProfessionalsException;
import com.g5.tdp2.myhealthapp.util.CrmJsonArrayRequest;
import com.g5.tdp2.myhealthapp.util.JsonParser;
import com.g5.tdp2.myhealthapp.util.LocationManager;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

public class WebSearchProfessionals implements SearchProfessionals {
    private String baseUrl;
    private RequestQueue requestQueue;

    public WebSearchProfessionals(String baseUrl, RequestQueue requestQueue) {
        this.baseUrl = baseUrl;
        this.requestQueue = requestQueue;
    }

    @Override
    public void searchProfessionals(
            ProfessionalSearchForm form,
            Consumer<List<Professional>> succCallback,
            Consumer<Exception> errCallback) throws SearchProfessionalsException {
        try {
            form.validate();
        } catch (IllegalStateException e) {
            errCallback.accept(new SearchProfessionalsException(INVALID_FORM, e));
            return;
        }

        try {
            String url = form.concat(baseUrl + "?", "=", "&", this::encode);
            CrmJsonArrayRequest request = new CrmJsonArrayRequest(Request.Method.GET, url, null, response -> {
                Log.i("WebSearchProfessionals-response", response.toString());
                Professional[] professionals = JsonParser.INSTANCE.readValue(response.toString(), Professional[].class);
                succCallback.accept(Arrays.asList(professionals));
            }, error -> {
                Exception ex = Optional.ofNullable(error).map(err -> {
                    Log.e("WebSearchProfessionals-error", err.toString());
                    return new SearchProfessionalsException(UNKNOWN_ERROR);
                }).orElseGet(() -> new SearchProfessionalsException(INTERNAL_ERROR));
                errCallback.accept(ex);
            });
            requestQueue.add(request);
        } catch (Exception e) {
            errCallback.accept(new SearchProfessionalsException(INTERNAL_ERROR));
        }
    }

    @Override
    public void searchProfessionals(
            ProfessionalWdistForm form,
            Consumer<List<Professional>> succCallback,
            Consumer<Exception> errCallback) throws SearchProfessionalsException {
        try {
            form.validate();
        } catch (IllegalStateException e) {
            errCallback.accept(new SearchProfessionalsException(INVALID_FORM, e));
            return;
        }

        try {
            String url = form.concat(baseUrl + "?", "=", "&", this::encode);
            CrmJsonArrayRequest request = new CrmJsonArrayRequest(Request.Method.GET, url, null, response -> {
                Log.i("WebSearchProfessionals-response", response.toString());
                Professional[] professionals = JsonParser.INSTANCE.readValue(response.toString(), Professional[].class);

                double distance = form.getDistance();
                Place myPlace = form.getMyPlace();
                Function<Place, Function<Place, Double>> f = p1 -> p2 ->
                        LocationManager.INSTANCE.distanceMts(p1.getLat(), p1.getLon(), p2.getLat(), p2.getLon());
                List<Professional> okProfs = Arrays.stream(professionals)
                        .filter(Provider::hasOffice) // descartamos los profesionales sin oficina
                        .flatMap(p -> p.flattenByOffice().stream())
                        .filter(p -> myPlace.distanceMts(p.getMainOffice(), f) < distance)
                        .collect(Collectors.toList());

                succCallback.accept(okProfs);
            }, error -> {
                Exception ex = Optional.ofNullable(error).map(err -> {
                    Log.e("WebSearchProfessionals-error", err.toString());
                    return new SearchProfessionalsException(UNKNOWN_ERROR);
                }).orElseGet(() -> new SearchProfessionalsException(INTERNAL_ERROR));
                errCallback.accept(ex);
            });
            requestQueue.add(request);
        } catch (Exception e) {
            errCallback.accept(new SearchProfessionalsException(INTERNAL_ERROR));
        }
    }

    private String encode(String s) {
        try {
            return URLEncoder.encode(s, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new IllegalArgumentException("Imposible encodear " + s + " en UTF-8", e);
        }
    }
}
