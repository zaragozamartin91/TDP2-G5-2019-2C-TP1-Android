package com.g5.tdp2.myhealthapp.service;

import com.g5.tdp2.myhealthapp.entity.Member;
import com.g5.tdp2.myhealthapp.entity.MemberCredentials;
import com.g5.tdp2.myhealthapp.usecase.LoginMember;
import com.g5.tdp2.myhealthapp.usecase.LoginMemberException;

public class LoginMemberStub implements LoginMember {

    @Override
    public Member loginMember(MemberCredentials memberCredentials) throws LoginMemberException {
        memberCredentials.validate();

        if (memberCredentials.getId() != 1234L) {
            /* Usuario 1234 es el unico valido para hacer pruebas */
            throw new LoginMemberException(WRONG_CREDENTIALS);
        }

        return new Member();
    }
}
