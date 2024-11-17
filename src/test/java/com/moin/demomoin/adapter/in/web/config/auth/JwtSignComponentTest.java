package com.moin.demomoin.adapter.in.web.config.auth;

import static com.moin.demomoin.adapter.out.persistence.entity.MoinIdType.REG_NO;
import static org.mockito.BDDMockito.given;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.moin.demomoin.adapter.out.persistence.entity.MoinUser;
import com.moin.demomoin.application.port.in.SignupCommand;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.test.StepVerifier;

@DisplayName("JWT 토큰 검증")
class JwtSignComponentTest {

  @Mock
  JwtSignComponent jwtSignComponent;
  AutoCloseable autoCloseable;
  MoinUser user = new MoinUser(
        new SignupCommand(
            "moin@themoin.com", "password", "moin", REG_NO, "000706-1492312")
    );

  private static String testToken(Instant expiresAt) {
    var algorithm = Algorithm.HMAC256(JwtSecretConstant.DEFAULT_SECRET.getCode());
    return JWT.create()
        .withClaim("userId", "moin@themoin.com")
        .withClaim("name", "moin")
        .withClaim("idType", REG_NO.getCode())
        .withClaim("type", "token")
        .withClaim("role", "user")
        .withExpiresAt(expiresAt)
        .withIssuer("moin")
        .withSubject("moin")
        .sign(algorithm);
  }

  @BeforeEach
  void setUp() {
    autoCloseable = MockitoAnnotations.openMocks(this);
  }

  @AfterEach
  void tearDown() throws Exception {
    autoCloseable.close();
  }

  @Test
  @DisplayName("토큰 생성 테스트")
  void testShouldBeSucceedToken() {
    var t = testToken(Instant.now().plus(30, ChronoUnit.MINUTES));
    StepVerifier.create(JwtVerifier.check(t))
        .expectNextCount(1)
        .verifyComplete();

  }

  @Test
  @DisplayName("토큰 만료 테스트")
  void testShouldBeExpireToken() {
    var t = testToken(Instant.now().minus(1, ChronoUnit.MINUTES));

    given(jwtSignComponent.createToken(user)).willReturn(t);
    var token = jwtSignComponent.createToken(user);
    StepVerifier.create(JwtVerifier.check(token))
        .expectNextCount(0)
        .verifyComplete();
  }
}