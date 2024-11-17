package com.moin.demomoin.domain;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.moin.demomoin.application.port.in.TransferQuoteCommand;
import java.math.BigDecimal;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

class MoinTransferCalculationUnitTest {

  @Test
  void testShouldBeSameEstimatedAndCalculatedAmountUSD() {
    var command = new TransferQuoteCommand(605_000, MoinCurrencyType.USD);

    assertEquals(605_000, command.amount());
    assertEquals(MoinCurrencyType.USD, command.targetCurrency());

    StepVerifier.create(
            Flux.just(new ExchangeInformation("FRX.KRWUSD", MoinCurrencyType.USD,
                    BigDecimal.valueOf(1317.00), BigDecimal.valueOf(1)))
                .collectList()
                .map(exchangeInformation -> new MoinTransferCalculationUnit(command,
                    exchangeInformation.stream().collect(
                        Collectors.toMap(ExchangeInformation::currencyCode, x -> x)))))
        .consumeNextWith(next -> assertAll(
                () -> assertEquals(new BigDecimal("1317.0"), next.getExchangeRate()),
                () -> assertEquals(new BigDecimal("1000.00"), next.getFixedFee()),
                () -> assertEquals(new BigDecimal("0.002"), next.getFeeRate()),
                () -> assertEquals(new BigDecimal("1.68"), next.getCommissionAmount()),
                () -> assertEquals(new BigDecimal("457.70"), next.getTargetAmount()),
                () -> assertEquals(new BigDecimal("1317.0"), next.getUsdExchangeRate()),
                () -> assertEquals(new BigDecimal("457.70"), next.getUsdAmount())
            )
        )
        .verifyComplete();
  }

  @Test
  void testShouldBeSameEstimatedAndCalculatedAmountJPY() {
    var command = new TransferQuoteCommand(300_000, MoinCurrencyType.JPY);

    assertEquals(300_000, command.amount());
    assertEquals(MoinCurrencyType.JPY, command.targetCurrency());

    StepVerifier.create(
            Flux.just(new ExchangeInformation("FRX.KRWJPY", MoinCurrencyType.JPY,
                        BigDecimal.valueOf(9.0565), BigDecimal.valueOf(100)),
                    new ExchangeInformation("FRX.KRWUSD", MoinCurrencyType.USD, BigDecimal.valueOf(1317.00),
                        BigDecimal.valueOf(1)))
                .collectList()
                .map(exchangeInformation -> new MoinTransferCalculationUnit(command,
                    exchangeInformation.stream().collect(
                        Collectors.toMap(ExchangeInformation::currencyCode, x -> x)))))
        .consumeNextWith(next -> assertAll(
            () -> assertEquals(new BigDecimal("9.0565"), next.getExchangeRate()),
            () -> assertEquals(new BigDecimal("3000"), next.getFixedFee()),
            () -> assertEquals(new BigDecimal("0.005"), next.getFeeRate()),
            () -> assertEquals(new BigDecimal("497"), next.getCommissionAmount()),
            () -> assertEquals(new BigDecimal("32628"), next.getTargetAmount()),
            () -> assertEquals(new BigDecimal("1317.0"), next.getUsdExchangeRate()),
            () -> assertEquals(new BigDecimal("226.58"), next.getUsdAmount())
        ))
        .verifyComplete();
  }


}