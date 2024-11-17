package com.moin.demomoin.adapter.out.persistence.entity;

import com.moin.demomoin.common.IdGenerator;
import com.moin.demomoin.domain.MoinCurrencyType;
import com.moin.demomoin.domain.MoinStatusCode;
import com.moin.demomoin.domain.MoinTransferCalculationUnit;
import com.moin.demomoin.domain.dto.MoinTransferDto;
import com.moin.demomoin.domain.dto.MoinTransferDto.QuoteResponse;
import com.moin.demomoin.domain.exception.MoinInvalidArgumentException;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.relational.core.mapping.Table;

@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table("moin_transfer_quote")
public class MoinTransferQuote {

  private Long id;
  private String userId;
  private Instant requestedDate;
  private Instant expireTime;
  private String sourceCurrency = "KRW";
  private MoinCurrencyType targetCurrency;
  private BigDecimal sourceAmount;
  private BigDecimal exchangeRate;
  private BigDecimal targetAmount;
  private BigDecimal fee;
  private BigDecimal feeRate;
  private BigDecimal usdExchangeRate;
  private BigDecimal usdAmount;

  public MoinTransferQuote(String userId, Instant requestedDate,
      MoinTransferCalculationUnit calculationUnit) {
    this(IdGenerator.generate(), userId, requestedDate, requestedDate.plus(10, ChronoUnit.MINUTES),
        calculationUnit.getSourceCurrency(), calculationUnit.getTargetCurrency(),
        calculationUnit.getSourceAmount(),
        calculationUnit.getExchangeRate()
        , calculationUnit.getTargetAmount(), calculationUnit.getFixedFee(),
        calculationUnit.getFeeRate(), calculationUnit.getUsdExchangeRate(), calculationUnit.getUsdAmount());
  }

  public MoinTransferRequest toTransferRequest() {
    return MoinTransferRequest.from(userId, id, sourceAmount, fee, feeRate, usdExchangeRate,
        usdAmount, targetCurrency, exchangeRate, targetAmount, requestedDate);
  }

  public MoinTransferQuote validate() {
    if (sourceAmount.compareTo(BigDecimal.ZERO) <= 0) {
      throw new MoinInvalidArgumentException(MoinStatusCode.NEGATIVE_NUMBER);
    }

    if (expireTime.isBefore(Instant.now())) {
      throw new MoinInvalidArgumentException(MoinStatusCode.QUOTE_EXPIRED);
    }

    return this;
  }

  public MoinTransferRequest validateThenToTransferRequest() {
    return this.validate()
        .toTransferRequest();
  }

  public MoinTransferDto.QuoteResponse toResponse() {
    return new QuoteResponse(id, exchangeRate, expireTime, targetAmount);
  }
}
