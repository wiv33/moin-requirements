package com.moin.demomoin.util;

import com.moin.demomoin.domain.ExchangeInformation;
import com.moin.demomoin.domain.MoinCurrencyType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

class ExchangeApiTest {

  @Test
  void testShouldGetExchangeInformation() {
    Flux<ExchangeInformation> exchangeRate = ExchangeApi.getExchangeRate(
            MoinCurrencyType.USD, MoinCurrencyType.JPY)
        .log();

    StepVerifier.create(exchangeRate)
        .assertNext(Assertions::assertNotNull)
        .assertNext(Assertions::assertNotNull)
        .verifyComplete();
  }

  @Test
  void testShouldGetExchangeInformationUSD() {
    Flux<ExchangeInformation> exchangeRate = ExchangeApi.getExchangeRate(MoinCurrencyType.USD)
        .log();

    StepVerifier.create(exchangeRate)
        .assertNext(Assertions::assertNotNull)
        .verifyComplete();
  }
}