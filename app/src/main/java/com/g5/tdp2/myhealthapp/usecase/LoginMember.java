package com.g5.tdp2.myhealthapp.usecase;

import com.g5.tdp2.myhealthapp.entity.Member;
import com.g5.tdp2.myhealthapp.entity.MemberCredentials;

import java.util.function.Consumer;

/**
 * Caso de uso de inicio de sesion de usuario
 */
public interface LoginMember extends Usecase {
    void loginMember(MemberCredentials memberCredentials, Consumer<Member> succCallback, Consumer<Exception> errCallback);
}
