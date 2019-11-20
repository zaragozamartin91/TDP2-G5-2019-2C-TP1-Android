package com.g5.tdp2.myhealthapp.service;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.g5.tdp2.myhealthapp.entity.Place;
import com.g5.tdp2.myhealthapp.entity.Provider;
import com.g5.tdp2.myhealthapp.entity.Sanatorium;
import com.g5.tdp2.myhealthapp.entity.SanatoriumWdistForm;
import com.g5.tdp2.myhealthapp.usecase.SearchProvidersException;
import com.g5.tdp2.myhealthapp.usecase.SearchSanatoriums;
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

public class WebSearchSanatoriums implements SearchSanatoriums {
    private String baseUrl;
    private RequestQueue requestQueue;

    public WebSearchSanatoriums(String baseUrl, RequestQueue requestQueue) {
        this.baseUrl = baseUrl;
        this.requestQueue = requestQueue;
    }

    @Override
    public void searchSanatoriums(
            SanatoriumWdistForm form,
            Consumer<List<Sanatorium>> succCallback,
            Consumer<Exception> errCallback) throws SearchProvidersException {
        try {
            form.validate();
        } catch (IllegalStateException e) {
            errCallback.accept(new SearchProvidersException(INVALID_FORM, e));
            return;
        }

        try {
            String url = form.concat(baseUrl + "?", "=", "&", this::encode);
            CrmJsonArrayRequest request = new CrmJsonArrayRequest(Request.Method.GET, url, null, response -> {
                Log.i("WebSearchSanatoriums-response", response.toString());
                Sanatorium[] sanatoriums = JsonParser.INSTANCE.readValue(response.toString(), Sanatorium[].class);

                double distance = form.getDistance();
                Place myPlace = form.getMyPlace();
                Function<Place, Function<Place, Double>> f = p1 -> p2 ->
                        LocationManager.INSTANCE.distanceMts(p1.getLat(), p1.getLon(), p2.getLat(), p2.getLon());
                List<Sanatorium> okSans = Arrays.stream(sanatoriums)
                        .filter(Provider::hasOffice) // descartamos los sanatorios sin oficina
                        .flatMap(p -> p.flattenByOffice().stream())
                        .filter(p -> myPlace.distanceMts(p.getMainOffice(), f) < distance)
                        .collect(Collectors.toList());

                succCallback.accept(okSans);
            }, error -> {
                Exception ex = Optional.ofNullable(error).map(err -> {
                    Log.e("WebSearchSanatoriums-error", err.toString());
                    return new SearchProvidersException(UNKNOWN_ERROR);
                }).orElseGet(() -> new SearchProvidersException(INTERNAL_ERROR));
                errCallback.accept(ex);
            });
            requestQueue.add(request);
        } catch (Exception e) {
            errCallback.accept(new SearchProvidersException(INTERNAL_ERROR));
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
