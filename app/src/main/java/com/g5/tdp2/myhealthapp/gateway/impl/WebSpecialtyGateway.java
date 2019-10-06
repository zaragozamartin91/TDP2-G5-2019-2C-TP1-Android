package com.g5.tdp2.myhealthapp.gateway.impl;

import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.g5.tdp2.myhealthapp.entity.Specialty;
import com.g5.tdp2.myhealthapp.gateway.GatewayException;
import com.g5.tdp2.myhealthapp.gateway.SpecialtyGateway;
import com.g5.tdp2.myhealthapp.util.CrmJsonArrayRequest;
import com.g5.tdp2.myhealthapp.util.JsonParser;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

public class WebSpecialtyGateway implements SpecialtyGateway {
    private static final String LPREFIX = "WebSpecialtyGateway-";
    private String url;
    private RequestQueue requestQueue;

    public WebSpecialtyGateway(String url, RequestQueue requestQueue) {
        this.url = url;
        this.requestQueue = requestQueue;
    }

    @Override
    public void getSpecialties(Consumer<List<Specialty>> succCallback, Consumer<Exception> errCallback) {
        try {
            JsonArrayRequest request = new CrmJsonArrayRequest(url, response -> {
                Log.i(LPREFIX + "onResponse", response.toString());
                Specialty[] ss = JsonParser.INSTANCE.readValue(response.toString(), Specialty[].class);
                succCallback.accept(Arrays.asList(ss));
            }, error -> {
                Log.e(LPREFIX + "onErrorResponse", "" + error);
                Exception ex = Optional.ofNullable(error)
                        .map(e -> new GatewayException(UNKNOWN_ERROR))
                        .orElse(new GatewayException(INTERNAL_ERROR));
                errCallback.accept(ex);
            });
            requestQueue.add(request);
        } catch (Exception e) {
            Log.e(LPREFIX + "error", e.toString());
            errCallback.accept(new GatewayException(UNKNOWN_ERROR));
        }
    }
}
