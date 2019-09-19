package com.g5.tdp2.myhealthapp.service;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.g5.tdp2.myhealthapp.AppState;
import com.g5.tdp2.myhealthapp.entity.Member;
import com.g5.tdp2.myhealthapp.entity.MemberCredentials;
import com.g5.tdp2.myhealthapp.usecase.LoginMember;
import com.g5.tdp2.myhealthapp.usecase.LoginMemberException;
import com.g5.tdp2.myhealthapp.util.JsonParser;

import org.json.JSONObject;

import java.util.function.Consumer;

public class WebLoginMember implements LoginMember {
    private String url;
    private Context context;

    public WebLoginMember(String url, Context context) {
        this.url = url;
        this.context = context;
    }

    @Override
    public void loginMember(MemberCredentials memberCredentials, Consumer<Member> succCallback, Consumer<Exception> errCallback)
            throws IllegalStateException, LoginMemberException {
        try {
            memberCredentials.validate();

            RequestQueue requestQueue = Volley.newRequestQueue(context);
            String mRequestBody = new ObjectMapper().writeValueAsString(memberCredentials);

            JSONObject jsonObject = new JSONObject(mRequestBody);
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject, response -> {
                Log.i("WebLoginMember-onResponse", response.toString());
                LoginMemberJsonResponse loginResponse = JsonParser.INSTANCE.readValue(response.toString(), LoginMemberJsonResponse.class);
                succCallback.accept(loginResponse.member);
                AppState.INSTANCE.putToken(loginResponse.token);
            }, error -> {
                Log.e("WebLoginMember-onErrorResponse", error.toString());
                switch (error.networkResponse.statusCode) {
                    case 400:
                    case 403:
                        errCallback.accept(new LoginMemberException(WRONG_CREDENTIALS));
                        break;
                    default:
                        errCallback.accept(new LoginMemberException(UNKNOWN_ERROR));
                }
            });
            requestQueue.add(jsonObjectRequest);
        } catch (IllegalStateException e) {
            throw new LoginMemberException(e.getMessage());
        } catch (Exception e) {
            throw new LoginMemberException(UNKNOWN_ERROR, e);
        }
    }

}

class LoginMemberJsonResponse {
    final Member member;
    final String token;

    @JsonCreator
    public LoginMemberJsonResponse(
            @JsonProperty("afiliado") Member member,
            @JsonProperty("token") String token) {
        this.member = member;
        this.token = token;
    }
}