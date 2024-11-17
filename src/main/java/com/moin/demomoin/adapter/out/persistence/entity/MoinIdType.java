package com.moin.demomoin.adapter.out.persistence.entity;

import java.math.BigDecimal;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum MoinIdType {
  REG_NO("REG_NO", "개인", BigDecimal.valueOf(1_000)),
  BUSINESS_NO("BUSINESS_NO", "법인", BigDecimal.valueOf(5_000)),
  ;

  private final String code;
  private final String description;
  private final BigDecimal overMaximumTransferDollar;

  public boolean isOverMaximumTransferDollar(BigDecimal transferAmount) {
    return transferAmount.compareTo(overMaximumTransferDollar) > 0;
  }
}
