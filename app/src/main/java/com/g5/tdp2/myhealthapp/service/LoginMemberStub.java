package com.g5.tdp2.myhealthapp.service;

import com.g5.tdp2.myhealthapp.entity.Member;
import com.g5.tdp2.myhealthapp.entity.MemberCredentials;
import com.g5.tdp2.myhealthapp.usecase.LoginMember;

public class LoginMemberStub implements LoginMember {
    @Override
    public Member loginMember(MemberCredentials memberCredentials) {
        memberCredentials.validate();
        return new Member();
    }
}
