package com.moin.demomoin.adapter.out.persistence;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.moin.demomoin.adapter.out.persistence.entity.MoinIdType;
import com.moin.demomoin.application.port.in.TransferQuoteCommand;
import com.moin.demomoin.application.port.in.TransferRequestCommand;
import com.moin.demomoin.domain.MoinCurrencyType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest;
import org.springframework.context.annotation.Import;
import reactor.test.StepVerifier;

@DataR2dbcTest
@Import(TransferAdapter.class)
@DisplayName("Transfer Quote, Transfer Request 테스트")
class TransferAdapterTest {

  @Autowired
  TransferAdapter adapter;

  @Test
  @DisplayName("Transfer Quote 저장 성공")
  void testShouldBeSucceedSave() {
    var userId = "moin@themoin.com";
    var command = new TransferQuoteCommand(605_000, MoinCurrencyType.USD);
    StepVerifier.create(adapter.saveTransferQuote(userId, command).log())
        .consumeNextWith(nextResponse -> {
          Assertions.assertAll(() -> {
            assertNotNull(nextResponse.id());
            assertNotNull(nextResponse.expireTime());
            assertNotNull(nextResponse.targetAmount());
            assertNotNull(nextResponse.exchangeRate());
          });
        })
        .verifyComplete();
  }

  @Test
  @DisplayName("transfer request 금액 초과 저장 실패 - LIMIT_EXCESS")
  void testShouldBeFailureTransferRequest_LIMIT_EXCESS() {
    // given
    var userId = "moin@themoin.com";
    var firstSavedRequest = adapter.saveTransferQuote(userId,
        new TransferQuoteCommand(905_000, MoinCurrencyType.USD));
    firstSavedRequest
        .as(StepVerifier::create)
        .expectNextCount(1)
        .verifyComplete();

    firstSavedRequest.flatMap(quoteResponse -> adapter.saveTransferRequest(userId,
                "moin", MoinIdType.REG_NO,
                new TransferRequestCommand(quoteResponse.id()))
            .log())
        .as(StepVerifier::create)
        .verifyComplete();

    var secondSavedRequest = adapter.saveTransferQuote(userId,
        new TransferQuoteCommand(605_000, MoinCurrencyType.USD));
    StepVerifier.create(secondSavedRequest.log())
        .expectNextCount(1)
        .verifyComplete();

    // when / then
    secondSavedRequest.flatMap(quoteResponse -> adapter.saveTransferRequest(userId,
                "moin", MoinIdType.REG_NO,
                new TransferRequestCommand(quoteResponse.id()))
            .log())
        .as(StepVerifier::create)
        .expectErrorMessage("LIMIT_EXCESS")
        .verify();
  }

  @Test
  @DisplayName("transfer request 재사용 저장 실패 - INVALID_ALREADY_USED_QUOTE")
  void testShouldFailureTransferRequest_INVALID_ALREADY_USED_QUOTE() {
    // given
    var userId = "moin@themoin.com";
    var savedTransferQuote = adapter.saveTransferQuote(userId,
            new TransferQuoteCommand(400_000, MoinCurrencyType.USD))
        .share();

    savedTransferQuote
        .as(StepVerifier::create)
        .expectNextCount(1)
        .verifyComplete();
    savedTransferQuote.flatMap(quoteResponse -> adapter.saveTransferRequest(userId,
            "moin", MoinIdType.REG_NO,
            new TransferRequestCommand(quoteResponse.id())))
        .log()
        .as(StepVerifier::create)
        .verifyComplete();


    // when / then
    savedTransferQuote.flatMap(quoteResponse ->
            adapter.saveTransferRequest(userId,
                "moin", MoinIdType.REG_NO,
                new TransferRequestCommand(quoteResponse.id())))
        .as(StepVerifier::create)
        .expectErrorMessage("INVALID_ALREADY_USED_QUOTE")
        .verify();
  }
}