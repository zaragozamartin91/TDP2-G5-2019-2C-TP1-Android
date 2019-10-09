package com.g5.tdp2.myhealthapp.util;

import androidx.annotation.Nullable;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;

public class CrmJsonArrayRequest extends JsonArrayRequest {
    private static final int TIMEOUT_MS = 3000; // timeout en milisegundos
    private static final int MAX_NUM_RETRIES = 3; // maxima cantidad de reintentos

    public CrmJsonArrayRequest(String url, Response.Listener<JSONArray> listener, @Nullable Response.ErrorListener errorListener) {
        super(url, listener, errorListener);
        this.setRetryPolicy(new DefaultRetryPolicy(TIMEOUT_MS, MAX_NUM_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    public CrmJsonArrayRequest(int method, String url, @Nullable JSONArray jsonRequest, Response.Listener<JSONArray> listener, @Nullable Response.ErrorListener errorListener) {
        super(method, url, jsonRequest, listener, errorListener);
        this.setRetryPolicy(new DefaultRetryPolicy(TIMEOUT_MS, MAX_NUM_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }
}
