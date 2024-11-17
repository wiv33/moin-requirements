package com.moin.demomoin.domain;

import com.moin.demomoin.application.port.in.TransferQuoteCommand;
import java.math.BigDecimal;

public class MoinAmount {
  private final String sourceCurrency = "KRW";
  private MoinCurrencyType targetCurrency;
  private BigDecimal fee;
  private BigDecimal feeRate;
  private BigDecimal exchangeRate;
  private BigDecimal targetAmount;


  public MoinAmount(TransferQuoteCommand command, ExchangeInformation exchangeInfo) {
    this.targetCurrency = command.targetCurrency();
    var commission = MoinCommissionFactory.getCommission(targetCurrency,
        BigDecimal.valueOf(command.amount()));
    this.fee = commission.getFeeBasedAmount();
    this.feeRate = commission.getFeeRate();

  }
}
