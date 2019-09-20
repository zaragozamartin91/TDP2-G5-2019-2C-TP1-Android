package com.g5.tdp2.myhealthapp.usecase;

import com.g5.tdp2.myhealthapp.entity.Member;
import com.g5.tdp2.myhealthapp.entity.MemberCredentials;

import java.util.function.Consumer;


public interface LoginMember {
    String WRONG_CREDENTIALS = "WRONG_CREDENTIALS";
    String UNKNOWN_ERROR = "UNKNOWN_ERROR";

    void loginMember(MemberCredentials memberCredentials, Consumer<Member> succCallback, Consumer<Exception> errCallback);
}
