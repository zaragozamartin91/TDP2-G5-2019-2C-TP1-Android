package com.g5.tdp2.myhealthapp.gateway.impl;

import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.g5.tdp2.myhealthapp.entity.Zone;
import com.g5.tdp2.myhealthapp.gateway.GatewayException;
import com.g5.tdp2.myhealthapp.gateway.ZoneGateway;
import com.g5.tdp2.myhealthapp.util.JsonParser;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

public class WebZoneGateway implements ZoneGateway {
    private String url;
    private RequestQueue requestQueue;

    public WebZoneGateway(String url, RequestQueue requestQueue) {
        this.url = url;
        this.requestQueue = requestQueue;
    }

    @Override
    public void getZones(Consumer<List<Zone>> succCallback, Consumer<Exception> errCallback) {
        try {
            JsonArrayRequest request = new JsonArrayRequest(url, response -> {
                Log.i("WebZoneGateway-onResponse", response.toString());
                Zone[] ss = JsonParser.INSTANCE.readValue(response.toString(), Zone[].class);
                succCallback.accept(Arrays.asList(ss));
            }, error -> {
                Log.e("WebZoneGateway-onErrorResponse", "" + error);
                Exception ex = Optional.ofNullable(error)
                        .map(e -> new GatewayException(UNKNOWN_ERROR))
                        .orElse(new GatewayException(INTERNAL_ERROR));
                errCallback.accept(ex);
            });
            request.setRetryPolicy(new DefaultRetryPolicy(
                    DefaultRetryPolicy.DEFAULT_TIMEOUT_MS,
                    3,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(request);
        } catch (Exception e) {
            Log.e("WebZoneGateway-error", e.toString());
            errCallback.accept(new GatewayException(UNKNOWN_ERROR));
        }
    }
}
