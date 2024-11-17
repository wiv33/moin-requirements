package com.moin.demomoin.util;

import com.moin.demomoin.domain.ExchangeInformation;
import com.moin.demomoin.domain.MoinCurrencyType;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.experimental.UtilityClass;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * curl --location 'https://crix-api-cdn.upbit.com/v1/forex/recent?codes=,FRX.KRWJPY,FRX.KRWUSD'
 */
@UtilityClass
public class ExchangeInfoApi {

  private static final WebClient WEB_CLIENT = WebClient.create("https://crix-api-cdn.upbit.com");
  private static final String DEFAULT_PATH_PATTERN = "/v1/forex/recent?codes=,%s";

  public static Mono<Map<MoinCurrencyType, ExchangeInformation>> getExchangeRate(
      MoinCurrencyType... currencyType) {
    return retrieveExchangeInfo(Flux.just(currencyType).map(MoinCurrencyType::name))
        .log("get exchange info ----- >>>>")
        .collectList()
        .map(exchangeInfo -> exchangeInfo.stream().collect(
            Collectors.toMap(ExchangeInformation::currencyCode, x -> x)))
        ;
  }

  public static Mono<Map<MoinCurrencyType, ExchangeInformation>> getExchangeRate() {
    return getExchangeRate(MoinCurrencyType.USD, MoinCurrencyType.JPY);
  }

  private static Flux<ExchangeInformation> retrieveExchangeInfo(Flux<String> queryProvider) {
    return queryProvider
        .map("FRX.KRW%s"::formatted)
        .reduce((a, b) -> a + "," + b)
        .flatMapMany(query -> WEB_CLIENT.get()
            .uri(DEFAULT_PATH_PATTERN.formatted(query))
            .retrieve()
            .bodyToFlux(ExchangeInformation.class));
  }

  ;

}
