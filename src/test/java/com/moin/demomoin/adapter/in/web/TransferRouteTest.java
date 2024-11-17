package com.moin.demomoin.adapter.in.web;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import com.moin.demomoin.application.handler.TransferHandler;
import com.moin.demomoin.application.port.in.TransferQuoteCommand;
import com.moin.demomoin.domain.MoinCurrencyType;
import com.moin.demomoin.domain.MoinResponse;
import com.moin.demomoin.domain.dto.MoinTransferDto;
import com.moin.demomoin.domain.dto.MoinTransferDto.ListResponse;
import com.moin.demomoin.domain.dto.MoinTransferDto.RequestResponse;
import com.moin.demomoin.util.InstantUtil;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.server.ServerResponse;

@WebFluxTest(TransferRoute.class)
@DisplayName("TransferRoute 필드 검증")
class TransferRouteTest extends RouteBaseTest {

  @Autowired
  WebTestClient testClient;

  @MockBean
  TransferHandler handler;

  @Test
  void testShouldBeExistsQuoteField() {
    given(handler.quote(any())).willReturn(ServerResponse.ok()
        .contentType(MediaType.APPLICATION_JSON)
        .bodyValue(
            MoinResponse.mergeBody(Map.of("quote", new MoinTransferDto.QuoteResponse(172312311323L,
                BigDecimal.valueOf(1371.00), Instant.now(), BigDecimal.valueOf(605_000))))));

    testClient.post()
        .uri("/transfer/quote")
        .contentType(MediaType.APPLICATION_JSON)
        .header("Authorization", createToken())
        .accept(MediaType.ALL)
        .bodyValue(new TransferQuoteCommand(605_000, MoinCurrencyType.USD))
        .exchange()
        .expectBody()
        .jsonPath("$.resultCode", "200").exists()
        .jsonPath("$.resultMsg", "OK").exists()
        .jsonPath("$.quote").exists()
        .jsonPath("$.quote.id").exists()
        .jsonPath("$.quote.exchangeRate", "1371.00").exists()
        .jsonPath("$.quote.targetAmount", "605_000").exists()
    ;
  }

  @Test
  void testShouldBeExistsTransferRequest() {
    given(handler.request(any())).willReturn(ServerResponse.ok()
        .contentType(MediaType.APPLICATION_JSON)
        .bodyValue(MoinResponse.ok()));

    testClient.post()
        .uri("/transfer/request")
        .contentType(MediaType.APPLICATION_JSON)
        .header("Authorization", createToken())
        .accept(MediaType.ALL)
        .exchange()
        .expectBody()
        .jsonPath("$.resultCode", "200").exists()
        .jsonPath("$.resultMsg", "OK").exists();
  }

  @Test
  void testShouldGetTransferListByUser() {
    /*
    [
    {
      "sourceAmount": 605000.0,
      "fee": 1000.0,
      "usdExchangeRate": 1370.4,
      "usdAmount": 439.87,
      "targetCurrency": "USD",
      "exchangeRate": 1370.4,
      "targetAmount": 439.87,
      "requestedDate": "2024-08-13 17:40:49",
      "feeRate": "0.20%"
    },
    {
      "sourceAmount": 300000.0,
      "fee": 3000.0,
      "usdExchangeRate": 1370.4,
      "usdAmount": 217.74,
      "targetCurrency": "JPY",
      "exchangeRate": 927.42,
      "targetAmount": 318.0,
      "requestedDate": "2024-08-13 17:42:37",
      "feeRate": "0.50%"
    }
  ]
     */
    var mockResponse = new ListResponse("moin@themoin.com", "moin",
        List.of(
            new RequestResponse(
                BigDecimal.valueOf(605_000), BigDecimal.valueOf(1000),
                BigDecimal.valueOf(0.002),
                BigDecimal.valueOf(439.87), MoinCurrencyType.USD,
                BigDecimal.valueOf(1370.4),
                BigDecimal.valueOf(439.87), InstantUtil.toLocalDateTime(Instant.now()),
                "0.2%"
            ),
            new RequestResponse(
                BigDecimal.valueOf(300_000), BigDecimal.valueOf(3000),
                BigDecimal.valueOf(0.005),
                BigDecimal.valueOf(217.74), MoinCurrencyType.JPY,
                BigDecimal.valueOf(1370.4),
                BigDecimal.valueOf(318.00), InstantUtil.toLocalDateTime(Instant.now()),
                "0.5%"
            ),
            new RequestResponse(
                BigDecimal.valueOf(605_000), BigDecimal.valueOf(1000),
                BigDecimal.valueOf(0.002),
                BigDecimal.valueOf(439.87),
                MoinCurrencyType.USD,
                BigDecimal.valueOf(1370.4),
                BigDecimal.valueOf(439.87), InstantUtil.toLocalDateTime(Instant.now()),
                "0.2%"
            )
        ));
    given(handler.requestList(any())).willReturn(ServerResponse.ok()
        .contentType(MediaType.APPLICATION_JSON)
        .bodyValue(MoinResponse.mergeBody(mockResponse.toMap())));

    testClient.get()
        .uri("/transfer/list")
        .header("Authorization", createToken())
        .accept(MediaType.ALL)
        .exchange()
        .expectBody()
        .jsonPath("$.resultCode", "200").exists()
        .jsonPath("$.resultMsg", "OK").exists()
        .jsonPath("$.userId", "moin@themoin.com").exists()
        .jsonPath("$.name", "moin").exists()
        .jsonPath("$.history").exists()
        .jsonPath("$.history[0].sourceAmount", "605_000").exists()
        .jsonPath("$.history[0].fee", "3000").exists()
        .jsonPath("$.history[0].feeRate", "0.002").exists()
        .jsonPath("$.history[0].targetCurrency", "USD").exists()
        .jsonPath("$.history[0].exchangeRate", "1370.4").exists()
        .jsonPath("$.history[0].targetAmount", "439.87").exists()
        .jsonPath("$.history[0].requestedDate").exists()

        .jsonPath("$.history[1].sourceAmount", "300_000").exists()
        .jsonPath("$.history[1].fee", "3000").exists()
        .jsonPath("$.history[1].feeRate", "0.005").exists()
        .jsonPath("$.history[1].targetCurrency", "JPY").exists()
        .jsonPath("$.history[1].exchangeRate", "1370.4").exists()
        .jsonPath("$.history[1].targetAmount", "318.00").exists()
        .jsonPath("$.history[1].requestedDate").exists()

        .jsonPath("$.history[2].sourceAmount", "605_000").exists()
        .jsonPath("$.history[2].fee", "3000").exists()
        .jsonPath("$.history[2].feeRate", "0.002").exists()
        .jsonPath("$.history[2].targetCurrency", "USD").exists()
        .jsonPath("$.history[2].exchangeRate", "1370.4").exists()
        .jsonPath("$.history[2].targetAmount", "439.87").exists()
        .jsonPath("$.history[2].requestedDate").exists()


    ;
  }
}