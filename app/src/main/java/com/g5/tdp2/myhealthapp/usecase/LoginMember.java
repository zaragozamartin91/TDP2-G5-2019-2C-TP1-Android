package com.g5.tdp2.myhealthapp.usecase;

import com.g5.tdp2.myhealthapp.entity.Member;
import com.g5.tdp2.myhealthapp.entity.MemberCredentials;


public interface LoginMember {
    Member loginMember(MemberCredentials memberCredentials);
}
