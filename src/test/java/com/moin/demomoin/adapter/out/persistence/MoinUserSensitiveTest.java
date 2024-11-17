package com.moin.demomoin.adapter.out.persistence;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.springframework.data.relational.core.query.Query.query;

import com.moin.demomoin.adapter.in.web.config.db.R2dbcConfig;
import com.moin.demomoin.adapter.out.persistence.entity.MoinIdType;
import com.moin.demomoin.adapter.out.persistence.entity.MoinUser;
import com.moin.demomoin.domain.converter.MoinIdTypeConverter;
import com.moin.demomoin.domain.converter.MoinUserConverter;
import io.r2dbc.spi.ConnectionFactory;
import java.util.List;
import net.bytebuddy.build.ToStringPlugin.Exclude;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.data.r2dbc.convert.R2dbcCustomConversions;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.r2dbc.dialect.DialectResolver;
import org.springframework.data.relational.core.query.Criteria;
import reactor.test.StepVerifier;

@DataR2dbcTest
@DisplayName("민감정보 암호화")
@Import({TestR2dbcConfig.class})
public class MoinUserSensitiveText {

  public static final String userId = "moin@themoin.com";

  @Autowired
  private R2dbcEntityTemplate template;

  @BeforeEach
  void setUp() {
    var user = new MoinUser(userId, "password", "test", MoinIdType.REG_NO, "000706-1492312");
    StepVerifier.create(template.insert(user))
        .expectNextCount(1)
        .verifyComplete();
  }

  @Test
  @DisplayName("민감정보 암호화 테스트 - password, idValue")
  void testShouldSuccessEncryptSensitiveInfo() {
    template.select(MoinUser.class)
        .matching(query(Criteria.where("userId").is(userId)))
        .one()
        .log()
        .as(StepVerifier::create)
        .assertNext(user -> {
          assertEquals(userId, user.userId());
          assertNotEquals("password", user.passwordEnc());
          assertEquals(MoinIdType.REG_NO, user.idType());
          assertNotEquals("000706-1492312", user.idValueEnc());
        })
        .verifyComplete()
    ;
  }

}

@TestConfiguration
class TestR2dbcConfig {

  @Primary
  @Bean
  public R2dbcCustomConversions r2dbcCustomConversions(ConnectionFactory connectionFactory) {
    var dialect = DialectResolver.getDialect(connectionFactory);
    return R2dbcCustomConversions.of(dialect,
        List.of(
            new MoinUserConverter.Writing()
        )
    );
  }
}