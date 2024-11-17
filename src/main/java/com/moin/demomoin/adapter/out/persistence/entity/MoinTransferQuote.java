package com.moin.demomoin.adapter.out.persistence.entity;

import com.moin.demomoin.common.IdGenerator;
import com.moin.demomoin.domain.MoinStatusCode;
import com.moin.demomoin.domain.MoinTransferCalculationUnit;
import com.moin.demomoin.domain.exception.MoinInvalidArgumentException;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import lombok.AllArgsConstructor;
import org.springframework.data.relational.core.mapping.Table;

@AllArgsConstructor
@Table("moin_quote")
public class MoinQuote {

  private Long id;
  private String userId;
  private Instant requestDate;
  private Instant expireTime;
  private String sourceCurrency;
  private BigDecimal sourceAmount;
  private BigDecimal exchangeRate;
  private BigDecimal targetAmount;
  private BigDecimal fee;
  private String feeRate;

  public MoinQuote(String userId, Instant requestDate,
      MoinTransferCalculationUnit calculationUnit) {
    this(IdGenerator.generate(), userId, requestDate, requestDate.plus(10, ChronoUnit.MINUTES),
        calculationUnit.getSourceCurrency(), calculationUnit.getSourceAmount(),
        calculationUnit.getExchangeRate()
        , calculationUnit.getTargetAmount(), calculationUnit.getFixedFee(),
        calculationUnit.getFeeRate());
  }

  public MoinTransfer toTransfer() {
    if (expireTime.isAfter(Instant.now())) {
      throw new MoinInvalidArgumentException(MoinStatusCode.QUOTE_EXPIRED);
    }

    return new MoinTransfer(

    );
  }
}
