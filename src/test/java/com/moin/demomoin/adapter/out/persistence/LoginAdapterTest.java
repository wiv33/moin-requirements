package com.moin.demomoin.adapter.out.persistence;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.springframework.data.relational.core.query.Query.query;

import com.moin.demomoin.adapter.in.web.config.auth.JwtSignComponent;
import com.moin.demomoin.adapter.out.persistence.entity.MoinIdType;
import com.moin.demomoin.adapter.out.persistence.entity.MoinUser;
import com.moin.demomoin.application.port.in.SignupCommand;
import com.moin.demomoin.domain.exception.MoinInvalidArgumentException;
import com.moin.demomoin.domain.MoinStatusCode;
import com.moin.demomoin.domain.exception.MoinUserAlreadyExistsException;
import java.time.Duration;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.relational.core.query.Criteria;
import reactor.test.StepVerifier;

@DataR2dbcTest
@Import({LoginAdapter.class, JwtSignComponent.class})
@DisplayName("Login 테스트")
class LoginAdapterTest {

  String userId = "test@gmail.com";
  MoinUser just;
  @Autowired
  private LoginAdapter loginAdapter;
  @Autowired
  private R2dbcEntityTemplate template;

  @BeforeEach
  void setUp() {
    just = new MoinUser(userId, "password", "test", MoinIdType.REG_NO, "000706-1492312");
    StepVerifier.create(template.insert(just))
        .expectNextCount(1)
        .verifyComplete();
  }

  @AfterEach
  void tearDown() {
    StepVerifier.create(template.delete(MoinUser.class).all())
        .expectNextCount(1)
        .verifyComplete();
  }

  @Test
  @DisplayName("id 중복 테스트")
  void testShouldGetTrueIfAlreadyExistsUserId() {
    var publisher = loginAdapter.existsByUserId(userId);
    StepVerifier.create(publisher)
        .expectNext(Boolean.TRUE)
        .verifyComplete();
  }

  @Test
  @DisplayName("중복 아이디 생성 실패")
  void testShouldFailureSignupAlreadyExist() {
    template.select(MoinUser.class)
        .matching(query(Criteria.where("userId").is(userId)))
        .all()
        .log()
        .as(StepVerifier::create)
        .expectNextCount(1)
        .verifyComplete();

    var signupCommand = new SignupCommand(userId, "password", "test", MoinIdType.REG_NO,
        "000706-1492312");

    StepVerifier.create(loginAdapter.signup(signupCommand))
        .expectError(MoinUserAlreadyExistsException.class)
        .verify()
    ;
  }

  @Test
  @DisplayName("회원가입 성공 테스트")
  void testShouldSuccessSignupUser() {
    var signupCommand = new SignupCommand("sub" + userId, "password", "test", MoinIdType.REG_NO,
        "000706-1492312");

    StepVerifier.create(loginAdapter.signup(signupCommand))
        .expectNextCount(1)
        .verifyComplete()
    ;
  }

  @DisplayName("이메일 형식이 잘못된 경우 예외 발생")
  @ParameterizedTest
  @ValueSource(strings = {"test", "test@", "test@com", "test@com.", "test@.com", "test@.com.",
      "test@.com.1", "test@.com.1.", "test@.com.1.2", "#test@.com", "test@#com", "test@moin.com#"})
  void testShouldFailureSignupInvalidEmailFormat(String email) {
    var signupCommand = new SignupCommand(email, "password", "test", MoinIdType.REG_NO,
        "000706-1492312");

    StepVerifier.create(loginAdapter.signup(signupCommand))
        .expectErrorSatisfies(throwable -> {
          assertInstanceOf(MoinInvalidArgumentException.class, throwable);
          assertEquals(MoinStatusCode.INVALID_ARGUMENT_EMAIL_FORMAT.getCode(),
              throwable.getMessage());
        })
        .verify(Duration.ofSeconds(1))
    ;
  }

}