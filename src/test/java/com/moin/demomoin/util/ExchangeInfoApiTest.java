package com.moin.demomoin.util;

import com.moin.demomoin.domain.ExchangeInformation;
import com.moin.demomoin.domain.MoinCurrencyType;
import java.util.Map;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@DisplayName("환율 정보 API 테스트")
class ExchangeInfoApiTest {

  @Test
  @DisplayName("환율 정보 조회 테스트")
  void testShouldGetExchangeInformation() {
    Mono<Map<MoinCurrencyType, ExchangeInformation>> exchangeRate = ExchangeInfoApi.getExchangeRate(
            MoinCurrencyType.USD, MoinCurrencyType.JPY)
        .log();

    StepVerifier.create(exchangeRate)
        .assertNext(Assertions::assertNotNull)
        .verifyComplete();
  }

}