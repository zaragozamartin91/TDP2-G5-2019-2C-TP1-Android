package com.g5.tdp2.myhealthapp.service;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.g5.tdp2.myhealthapp.entity.Member;
import com.g5.tdp2.myhealthapp.entity.MemberCredentials;
import com.g5.tdp2.myhealthapp.usecase.LoginMember;
import com.g5.tdp2.myhealthapp.usecase.LoginMemberException;
import com.g5.tdp2.myhealthapp.util.JsonParser;
import com.mz.client.http.SimpleHttpClient;
import com.mz.client.http.SimpleHttpResponse;

import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.function.Consumer;

import static org.apache.http.entity.ContentType.APPLICATION_JSON;

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

        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpPost httpRequest = new HttpPost(url);
            StringEntity stringEntity = new StringEntity(new ObjectMapper().writeValueAsString(memberCredentials));
            stringEntity.setContentType(APPLICATION_JSON.getMimeType());
            httpRequest.setEntity(stringEntity);
            CloseableHttpResponse httpResponse = httpClient.execute(httpRequest);
            HttpEntity entity = httpResponse.getEntity();
            String body = entity == null ? null : EntityUtils.toString(entity);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        SimpleHttpResponse response = null;
        try {
            response = SimpleHttpClient.newPost(url)
                    .withJsonBody(new ObjectMapper().writeValueAsString(memberCredentials))
                    .execute();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

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
