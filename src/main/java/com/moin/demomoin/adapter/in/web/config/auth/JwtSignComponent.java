package com.moin.demomoin.adapter.in.web.config.auth;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.moin.demomoin.adapter.out.persistence.entity.MoinIdType;
import com.moin.demomoin.adapter.out.persistence.entity.MoinUser;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import org.springframework.stereotype.Component;

@Component
public class JwtSignComponent {

  @Deprecated
  public String createToken(String userId) {
    var algorithm = Algorithm.HMAC256(JwtSecretConstant.DEFAULT_SECRET.getCode());
    return JWT.create()
        .withClaim("userId", userId)
        .withClaim("name", "Anonymous")
        .withClaim("idType", MoinIdType.REG_NO.getCode())
        .withClaim("type", "token")
        .withClaim("role", "user")
        .withExpiresAt(Instant.now().plus(30, ChronoUnit.MINUTES))
        .withIssuer("moin")
        .withSubject("moin")
        .sign(algorithm);
  }

  public String createToken(MoinUser user) {
    var algorithm = Algorithm.HMAC256(JwtSecretConstant.DEFAULT_SECRET.getCode());
    return JWT.create()
        .withClaim("userId", user.userId())
        .withClaim("name", user.name())
        .withClaim("idType", user.idType().getCode())
        .withClaim("type", "token")
        .withClaim("role", "user")
        .withExpiresAt(Instant.now().plus(30, ChronoUnit.MINUTES))
        .withIssuer("moin")
        .withSubject("moin")
        .sign(algorithm);
  }
  public String createToken(MoinUser user, Instant expiresAt) {
    var algorithm = Algorithm.HMAC256(JwtSecretConstant.DEFAULT_SECRET.getCode());
    return JWT.create()
        .withClaim("userId", user.userId())
        .withClaim("name", user.name())
        .withClaim("idType", user.idType().getCode())
        .withClaim("type", "token")
        .withClaim("role", "user")
        .withExpiresAt(expiresAt)
        .withIssuer("moin")
        .withSubject("moin")
        .sign(algorithm);
  }

}
