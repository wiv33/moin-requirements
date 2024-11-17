package com.moin.demomoin.domain;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Currency;
import java.util.function.Function;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum MoinCommissionFactory {

  USD_UP_TO_100(MoinCurrencyType.USD,
      sourceAmount -> BigDecimal.valueOf(1_000_000).compareTo(sourceAmount) >= 0,
      BigDecimal.valueOf(1_000).setScale(getDefaultFractionDigits(MoinCurrencyType.USD),
          RoundingMode.HALF_UP),
      BigDecimal.valueOf(0.002)),
  USD_OVER_100(MoinCurrencyType.USD,
      sourceAmount -> BigDecimal.valueOf(1_000_000).compareTo(sourceAmount) < 0,
      BigDecimal.valueOf(3_000)
          .setScale(getDefaultFractionDigits(MoinCurrencyType.USD), RoundingMode.HALF_UP),
      BigDecimal.valueOf(0.001)),
  JPY_OVER_1(MoinCurrencyType.JPY,
      sourceAmount -> BigDecimal.valueOf(10_000).compareTo(sourceAmount) < 0,
      BigDecimal.valueOf(3_000)
          .setScale(getDefaultFractionDigits(MoinCurrencyType.JPY), RoundingMode.HALF_UP),
      BigDecimal.valueOf(0.005)),

  ;

  private final MoinCurrencyType currencyCode;
  private final Function<BigDecimal, Boolean> condition;
  @Getter
  private final BigDecimal fixedFee;
  @Getter
  private final BigDecimal feeRate;

  private static int getDefaultFractionDigits(MoinCurrencyType moinCurrencyType) {
    return Currency.getInstance(moinCurrencyType.name()).getDefaultFractionDigits();
  }

  public static MoinCommissionFactory getCommission(MoinCurrencyType currencyCode,
      BigDecimal sourceAmount) {
    for (MoinCommissionFactory commission : MoinCommissionFactory.values()) {
      if (currencyCode == commission.currencyCode && commission.condition.apply(sourceAmount)) {
        return commission;
      }
    }
    throw new IllegalArgumentException("Invalid source amount: " + sourceAmount);
  }

  public BigDecimal getTotalCommission(BigDecimal sourceAmount) {
    return
        sourceAmount.multiply(feeRate)
        .add(
            fixedFee
        )
        ;
  }


}
