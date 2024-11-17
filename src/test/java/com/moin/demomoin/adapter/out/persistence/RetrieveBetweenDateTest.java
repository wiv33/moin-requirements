package com.moin.demomoin.adapter.out.persistence;

import static org.springframework.data.relational.core.query.Criteria.where;

import com.moin.demomoin.adapter.out.persistence.entity.MoinTransferQuote;
import com.moin.demomoin.application.port.in.TransferQuoteCommand;
import com.moin.demomoin.domain.MoinCurrencyType;
import com.moin.demomoin.domain.MoinTransferCalculationUnit;
import com.moin.demomoin.util.ExchangeInfoApi;
import com.moin.demomoin.util.InstantUtil;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.relational.core.query.Query;
import reactor.test.StepVerifier;

@DataR2dbcTest
@DisplayName("날짜 범위 조회")
public class RetrieveBetweenDateTest {

  @Autowired
  R2dbcEntityTemplate template;

  static Stream<Arguments> availableDateWithExpectCount() {
    return Stream.of(
        Arguments.of(Instant.now().minus(1, ChronoUnit.DAYS), 0),
        Arguments.of(Instant.now().plus(1, ChronoUnit.DAYS), 0),
        Arguments.of(Instant.now(), 1)
    );
  }

  @DisplayName("날짜 범위 조회 테스트")
  @ParameterizedTest
  @MethodSource("availableDateWithExpectCount")
  void testShouldEqualDateAndExpectedCount(Instant baseTime, int expected) {
    var userId = "moin@themoin.com";
    ExchangeInfoApi.getExchangeRate().flatMap(unitMap -> template.insert(MoinTransferQuote.class)
            .using(new MoinTransferQuote(userId, baseTime,
                new MoinTransferCalculationUnit(new TransferQuoteCommand(605_000, MoinCurrencyType.USD),
                    unitMap))))
        .as(StepVerifier::create)
        .expectNextCount(1)
        .verifyComplete();

    StepVerifier.create(template.select(MoinTransferQuote.class).matching(Query.query(
            where("userId").is(userId).and(where("requested_date").between(InstantUtil.toStartTimestamp(),
                InstantUtil.toEndTimestamp())))).all().log())
        .expectNextCount(expected)
        .expectComplete()
        .verify();
  }
}
