package com.g5.tdp2.myhealthapp.gateway.impl;

import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.g5.tdp2.myhealthapp.entity.Checktype;
import com.g5.tdp2.myhealthapp.gateway.ChecktypeGateway;
import com.g5.tdp2.myhealthapp.gateway.GatewayException;
import com.g5.tdp2.myhealthapp.util.CrmJsonArrayRequest;
import com.g5.tdp2.myhealthapp.util.JsonParser;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

public class WebChecktypeGateway implements ChecktypeGateway {
    private String url;
    private RequestQueue requestQueue;

    public WebChecktypeGateway(String url, RequestQueue requestQueue) {
        this.url = url;
        this.requestQueue = requestQueue;
    }

    @Override
    public void getChecktypes(Consumer<List<Checktype>> succCallback, Consumer<Exception> errCallback) {
        try {
            JsonArrayRequest request = new CrmJsonArrayRequest(url, response -> {
                Log.i("WebChecktypeGateway-onResponse", response.toString());
                Checktype[] ss = JsonParser.INSTANCE.readValue(response.toString(), Checktype[].class);
                succCallback.accept(Arrays.asList(ss));
            }, error -> {
                Log.e("WebChecktypeGateway-onErrorResponse", "" + error);
                Exception ex = Optional.ofNullable(error)
                        .map(e -> new GatewayException(UNKNOWN_ERROR))
                        .orElse(new GatewayException(INTERNAL_ERROR));
                errCallback.accept(ex);
            });
            requestQueue.add(request);
        } catch (Exception e) {
            Log.e("WebChecktypeGateway-error", e.toString());
            errCallback.accept(new GatewayException(UNKNOWN_ERROR));
        }
    }
}
