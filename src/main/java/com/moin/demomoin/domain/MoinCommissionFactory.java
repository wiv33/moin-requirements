package com.moin.demomoin.domain;

import java.math.BigDecimal;
import java.util.function.Function;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor

public enum MoinCommissionFactory {
  
  USD_LIMIT_100("USD",sourceAmount -> sourceAmount.compareTo() ),
  USD_OVER_100("USD",sourceAmount -> sourceAmount > 1_000_000),

  ;
  private final String currencyCode;
  private final Function<BigDecimal, Boolean> condition;
}
