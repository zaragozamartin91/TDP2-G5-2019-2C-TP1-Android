package com.g5.tdp2.myhealthapp.service;

import com.g5.tdp2.myhealthapp.entity.Member;
import com.g5.tdp2.myhealthapp.entity.MemberCredentials;
import com.g5.tdp2.myhealthapp.usecase.LoginMember;
import com.g5.tdp2.myhealthapp.usecase.LoginMemberException;
import com.g5.tdp2.myhealthapp.util.DateFormatter;

import java.util.function.Consumer;

public class LoginMemberStub implements LoginMember {

    @Override
    public void loginMember(MemberCredentials memberCredentials, Consumer<Member> succCallback, Consumer<Exception> errCallback) {
        memberCredentials.validate();

        if (memberCredentials.getId() != 1234L) {
            /* Usuario 1234 es el unico valido para hacer pruebas */
            errCallback.accept(new LoginMemberException(WRONG_CREDENTIALS));
            return;
        }

        succCallback.accept(
                new Member(1, "Moni", "Argento", DateFormatter.YYYY_MM_DD.deserialize("1991-03-21"), "1234", "A110", 1234, "moni@argento.com")
                    .withToken("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZG4iOjM0MzE3Njc3LCJyb2xlIjoiYWZmaWxpYXRlIiwiaWF0IjoxNTcxMzM5ODUxfQ.pJoWqleEwItb4iJLMk-_OFy89dHGZ9PEbWIid69IkOw")
        );
    }
}
