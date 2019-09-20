package com.g5.tdp2.myhealthapp.service;

import com.g5.tdp2.myhealthapp.entity.Member;
import com.g5.tdp2.myhealthapp.entity.MemberCredentials;
import com.g5.tdp2.myhealthapp.usecase.LoginMember;
import com.g5.tdp2.myhealthapp.usecase.LoginMemberException;

import java.util.function.Consumer;

public class LoginMemberStub implements LoginMember {

    @Override
    public void loginMember(MemberCredentials memberCredentials, Consumer<Member> succCallback, Consumer<Exception> errCallback) {
        memberCredentials.validate();

        if (memberCredentials.getId() != 1234L) {
            /* Usuario 1234 es el unico valido para hacer pruebas */
            errCallback.accept(new LoginMemberException(WRONG_CREDENTIALS));
        }

        succCallback.accept(new Member());
    }
}
