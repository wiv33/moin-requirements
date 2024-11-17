package com.moin.demomoin.adapter.in.web.config.auth;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.moin.demomoin.domain.exception.MoinStatusCode;
import com.moin.demomoin.domain.exception.MoinUnauthorizedException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class JwtVerifierComponent {

  @Value("${jwt.secret}")
  private String secret;

  public Mono<DecodedJWT> check(String accessToken) {
    return Mono.<DecodedJWT>create(
            sink -> sink.success(JWT.require(Algorithm.HMAC256(secret)).build().verify(accessToken)))
        .onErrorResume(MoinUnauthorizedException.class,
            e -> Mono.error(new MoinUnauthorizedException(MoinStatusCode.UNAUTHORIZED)));
  }
}
