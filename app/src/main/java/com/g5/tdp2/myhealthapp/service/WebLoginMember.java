package com.g5.tdp2.myhealthapp.service;

import com.g5.tdp2.myhealthapp.entity.Member;
import com.g5.tdp2.myhealthapp.entity.MemberCredentials;
import com.g5.tdp2.myhealthapp.usecase.LoginMember;
import com.g5.tdp2.myhealthapp.util.JsonParser;
import com.mz.client.http.SimpleHttpClient;
import com.mz.client.http.SimpleHttpResponse;

public class WebLoginMember implements LoginMember {
    private String url;

    public WebLoginMember(String url) {
        this.url = url;
    }

    @Override
    public Member loginMember(MemberCredentials memberCredentials) {
        String username = memberCredentials.getId() + "";
        String password = memberCredentials.getPassword();

        SimpleHttpResponse response = SimpleHttpClient.newGet(url)
                .withBasicAuth(username, password)
                .execute();

        String bodyValue = response.getBodyValue();

        return JsonParser.INSTANCE.readValue(bodyValue, Member.class);
    }
}
