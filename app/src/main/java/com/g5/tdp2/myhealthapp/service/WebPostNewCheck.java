package com.g5.tdp2.myhealthapp.service;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.g5.tdp2.myhealthapp.entity.NewCheckForm;
import com.g5.tdp2.myhealthapp.usecase.PostNewCheck;
import com.g5.tdp2.myhealthapp.usecase.PostNewCheckException;
import com.g5.tdp2.myhealthapp.util.JsonParser;

import org.json.JSONObject;

import java.util.function.Consumer;

public class WebPostNewCheck implements PostNewCheck {
    private String url;
    private RequestQueue requestQueue;

    public WebPostNewCheck(String url, RequestQueue requestQueue) {
        this.url = url;
        this.requestQueue = requestQueue;
    }

    @Override
    public void postNewCheck(NewCheckForm newCheckForm, Runnable succCallback, Consumer<Exception> errCallback) {
        try {
            newCheckForm.validate();

            String reqBody = JsonParser.INSTANCE.writeValueAsString(newCheckForm);
            JSONObject jsonObject = new JSONObject(reqBody);
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject, response -> {
                Log.i("WebPostNewCheck-onResponse", response.toString());
                succCallback.run();
            }, error -> {
                Log.e("WebPostNewCheck-onErrorResponse", "" + error);
                errCallback.accept(new PostNewCheckException(INTERNAL_ERROR));
            });

            requestQueue.add(jsonObjectRequest);
        } catch (IllegalStateException e) {
            errCallback.accept(new PostNewCheckException(e.getMessage(), e));
        } catch (Exception e) {
            errCallback.accept(new PostNewCheckException(UNKNOWN_ERROR, e));
        }
    }
}
