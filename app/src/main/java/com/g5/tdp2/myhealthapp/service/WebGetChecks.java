package com.g5.tdp2.myhealthapp.service;

import android.util.Log;

import com.android.volley.RequestQueue;
import com.g5.tdp2.myhealthapp.entity.Check;
import com.g5.tdp2.myhealthapp.usecase.GetChecks;
import com.g5.tdp2.myhealthapp.usecase.GetChecksException;
import com.g5.tdp2.myhealthapp.util.CrmJsonArrayRequest;
import com.g5.tdp2.myhealthapp.util.JsonParser;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

public class WebGetChecks implements GetChecks {
    private String baseUrl;
    private RequestQueue requestQueue;

    public WebGetChecks(String baseUrl, RequestQueue requestQueue) {
        this.baseUrl = baseUrl;
        this.requestQueue = requestQueue;
    }

    @Override
    public void getChecks(
            int affiliateId, Consumer<List<Check>> succCallback, Consumer<Exception> errCallback) {
        try {
            String url = baseUrl + "?affiliate_id=" + affiliateId;

            CrmJsonArrayRequest request = CrmJsonArrayRequest.get(url, response -> {
                Log.i("WebGetChecks-onResponse", response.toString());
                Check[] checks = JsonParser.INSTANCE.readValue(response.toString(), Check[].class);
                succCallback.accept(Arrays.asList(checks));
            }, error -> {
                Exception ex = Optional.ofNullable(error).map(err -> {
                    Log.e("WebGetChecks-error", err.toString());
                    return new GetChecksException(UNKNOWN_ERROR, err.getCause());
                }).orElseGet(() -> new GetChecksException(INTERNAL_ERROR));
                errCallback.accept(ex);
            });

            requestQueue.add(request);
        } catch (Exception e) {
            errCallback.accept(new GetChecksException(INTERNAL_ERROR, e));
        }
    }
}
