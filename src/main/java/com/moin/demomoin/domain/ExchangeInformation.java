package com.moin.demomoin.domain;

import java.math.BigDecimal;

public record ExchangeInformation(
    String code,
    MoinCurrencyType currencyCode,
    BigDecimal basePrice,
    BigDecimal currencyUnit
) {

}
