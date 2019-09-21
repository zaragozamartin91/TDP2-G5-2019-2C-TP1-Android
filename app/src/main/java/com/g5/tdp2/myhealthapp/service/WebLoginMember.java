package com.g5.tdp2.myhealthapp.service;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.g5.tdp2.myhealthapp.entity.Member;
import com.g5.tdp2.myhealthapp.entity.MemberCredentials;
import com.g5.tdp2.myhealthapp.usecase.LoginMember;
import com.g5.tdp2.myhealthapp.usecase.LoginMemberException;
import com.g5.tdp2.myhealthapp.util.JsonParser;

import org.json.JSONObject;

import java.util.Optional;
import java.util.function.Consumer;

public class WebLoginMember implements LoginMember {
    private String url;
    private RequestQueue requestQueue;
    private Consumer<String> tokenConsumer = t -> {};

    public WebLoginMember(String url, RequestQueue requestQueue) {
        this.url = url;
        this.requestQueue = requestQueue;
    }

    public void setTokenConsumer(Consumer<String> tokenConsumer) {
        this.tokenConsumer = tokenConsumer;
    }

    @Override
    public void loginMember(MemberCredentials memberCredentials, Consumer<Member> succCallback, Consumer<Exception> errCallback) {
        try {
            memberCredentials.validate();
            String mRequestBody = new ObjectMapper().writeValueAsString(memberCredentials);

            JSONObject jsonObject = new JSONObject(mRequestBody);
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject, response -> {
                Log.i("WebLoginMember-onResponse", response.toString());
                LoginMemberJsonResponse loginResponse = JsonParser.INSTANCE.readValue(response.toString(), LoginMemberJsonResponse.class);
                succCallback.accept(loginResponse.member);
                tokenConsumer.accept(loginResponse.token);
            }, error -> {
                Log.e("WebLoginMember-onErrorResponse", "" + error);
                Exception ex = Optional.ofNullable(error)
                        .map(e -> e.networkResponse)
                        .map(n -> n.statusCode)
                        .map(s -> {
                                    switch (s) {
                                        case 400:
                                        case 403:
                                            return new LoginMemberException(WRONG_CREDENTIALS);
                                        default:
                                            return new LoginMemberException(UNKNOWN_ERROR);
                                    }
                                }
                        ).orElse(new LoginMemberException(INTERNAL_ERROR));
                errCallback.accept(ex);
            });
            requestQueue.add(jsonObjectRequest);
        } catch (IllegalStateException e) {
            errCallback.accept(new LoginMemberException(e.getMessage(), e));
        } catch (Exception e) {
            errCallback.accept(new LoginMemberException(UNKNOWN_ERROR, e));
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