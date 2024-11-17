package com.moin.demomoin.application.port.in;

import com.moin.demomoin.domain.MoinCurrencyType;
import com.moin.demomoin.domain.MoinStatusCode;
import com.moin.demomoin.domain.exception.MoinInvalidArgumentException;

public record TransferQuoteCommand(
    int amount,
    MoinCurrencyType targetCurrency
) {

  public TransferQuoteCommand {
    if (amount <= 0) {
      throw new MoinInvalidArgumentException(MoinStatusCode.NEGATIVE_NUMBER);
    }
  }

}
