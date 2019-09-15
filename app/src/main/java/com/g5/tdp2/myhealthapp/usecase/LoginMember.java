package com.g5.tdp2.myhealthapp.usecase;

import com.g5.tdp2.myhealthapp.entity.Member;
import com.g5.tdp2.myhealthapp.entity.MemberCredentials;


public interface LoginMember {
    String WRONG_CREDENTIALS = "WRONG_CREDENTIALS";
    String UNKNOWN_ERROR = "UNKNOWN_ERROR";

    Member loginMember(MemberCredentials memberCredentials)
            throws IllegalStateException, LoginMemberException;
}
