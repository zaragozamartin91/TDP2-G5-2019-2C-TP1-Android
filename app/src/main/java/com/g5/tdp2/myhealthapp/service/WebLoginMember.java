package com.g5.tdp2.myhealthapp.service;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.g5.tdp2.myhealthapp.entity.Member;
import com.g5.tdp2.myhealthapp.entity.MemberCredentials;
import com.g5.tdp2.myhealthapp.usecase.LoginMember;
import com.g5.tdp2.myhealthapp.usecase.LoginMemberException;
import com.g5.tdp2.myhealthapp.util.JsonParser;
import com.mz.client.http.SimpleHttpClient;
import com.mz.client.http.SimpleHttpResponse;

import java.util.function.Consumer;

public class WebLoginMember implements LoginMember {
    private String url;
    private Consumer<LoginMemberJsonResponse> successCallback;

    public WebLoginMember(String url, Consumer<LoginMemberJsonResponse> successCallback) {
        this.url = url;
        this.successCallback = successCallback;
    }

    @Override
    public Member loginMember(MemberCredentials memberCredentials) {
        memberCredentials.validate();

        String username = memberCredentials.getId() + "";
        String password = memberCredentials.getPassword();

        SimpleHttpResponse response = SimpleHttpClient.newGet(url)
                .withBasicAuth(username, password)
                .execute();

        switch (response.matchStatusCode()) {
            case UNAUTHORIZED:
            case S_4xx:
                throw new LoginMemberException(WRONG_CREDENTIALS);
            case S_2xx:
                return handleOkResponse(response);
        }

        throw new LoginMemberException(UNKNOWN_ERROR);
    }

    private Member handleOkResponse(SimpleHttpResponse response) {
        String bodyValue = response.getBodyValue();
        LoginMemberJsonResponse jsonResponse = JsonParser.INSTANCE.readValue(bodyValue, LoginMemberJsonResponse.class);
        successCallback.accept(jsonResponse);
        return jsonResponse.member;
    }

    public static class LoginMemberJsonResponse {
        public final Member member;
        public final String token;

        @JsonCreator
        public LoginMemberJsonResponse(
                @JsonProperty("afiliado") Member member,
                @JsonProperty("token") String token) {
            this.member = member;
            this.token = token;
        }
    }
}
