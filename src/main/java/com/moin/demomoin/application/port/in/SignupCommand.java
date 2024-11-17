package com.moin.demomoin.application.port.in;

import com.moin.demomoin.adapter.out.persistence.entity.MoinIdType;

/*
    "userId" : "{{userId}}", // 이메일 형식을 사용해주세요
    "password" :"{{password}}", // 패스워드
    "name" : "{{name}}",  // 이름
    "idType" : "{{idType}}", // Documentation 참조
    "idValue" : "{{idValue}}"  // Documentation 참조
 */
public record SignupCommand(
    String userId,
    String password,
    String name,
    MoinIdType idType,
    String idValue
) {

}
