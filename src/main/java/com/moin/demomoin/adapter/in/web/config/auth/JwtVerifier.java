package com.moin.demomoin.adapter.in.web.config.auth;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.experimental.UtilityClass;
import reactor.core.publisher.Mono;

@UtilityClass
public class JwtVerifier {

  public static Mono<DecodedJWT> check(String accessToken) {
    return Mono.<DecodedJWT>create(sink -> sink.success(
            JWT.require(Algorithm.HMAC256(JwtSecretConstant.DEFAULT_SECRET.getCode()))
                .build()
                .verify(accessToken)))
        .onErrorResume(e -> Mono.empty())
        .log("JwtVerifier -->>>>>>>>>")
        ;
  }
}
