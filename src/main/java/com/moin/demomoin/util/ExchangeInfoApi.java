package com.moin.demomoin.util;

import com.moin.demomoin.domain.ExchangeInformation;
import com.moin.demomoin.domain.MoinCurrencyType;
import lombok.experimental.UtilityClass;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

/**
 * curl --location 'https://crix-api-cdn.upbit.com/v1/forex/recent?codes=,FRX.KRWJPY,FRX.KRWUSD'
 */
@UtilityClass
public class ExchangeApi {

  private static final WebClient WEB_CLIENT = WebClient.create("https://crix-api-cdn.upbit.com");
  private static final String DEFAULT_PATH_PATTERN = "/v1/forex/recent?codes=,%s";

  public static Flux<ExchangeInformation> getExchangeRate(String... currencyCode) {
    return Flux.just(currencyCode).map(MoinCurrencyType::valueOf)
        .flatMap(ExchangeApi::getExchangeRate);
  }

  public static Flux<ExchangeInformation> getExchangeRate(MoinCurrencyType... currencyType) {
    return retrieveExchangeInfo(Flux.just(currencyType).map(MoinCurrencyType::name));
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
