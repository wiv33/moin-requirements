package com.moin.demomoin.adapter.out.persistence.entity;

import com.moin.demomoin.common.IdGenerator;
import com.moin.demomoin.domain.MoinCurrencyType;
import java.math.BigDecimal;
import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

/*
{
  "sourceAmount": 400000,  //원화 송금 요청액
  "fee" : 3000,  //송금 수수료
  "usdExchangeRate": 1301.01,
  "usdAmount": 305.14,  //usd 송금액
  "targetCurrency": "USD",  // 받는 환율 정보
  "exchangeRate": 1301.01,
  "targetAmount": 305.14,  // 받는 금액
  "requestedDate": "2023-12-01 10:30:21"  //송금 요청 시간
}
 */
@NoArgsConstructor
@AllArgsConstructor
@Table("moin_transfer")
public class MoinTransfer {

  @Id
  private Long id;
  private String userId;
  private Long quoteId;
  private BigDecimal sourceAmount;
  private BigDecimal fee;
  private BigDecimal feeRate;
  private BigDecimal usdExchangeRate;
  private BigDecimal usdAmount;
  private MoinCurrencyType targetCurrency;
  private BigDecimal exchangeRate;
  private BigDecimal targetAmount;
  private Instant requestedDate;

  public static MoinTransfer from(
      String userId, Long quoteId, BigDecimal sourceAmount, BigDecimal fee, BigDecimal feeRate,
      BigDecimal usdExchangeRate, BigDecimal usdAmount, MoinCurrencyType targetCurrency,
      BigDecimal exchangeRate, BigDecimal targetAmount, Instant requestedDate) {
    return new MoinTransfer(IdGenerator.generate(), userId, quoteId, sourceAmount, fee, feeRate,
        usdExchangeRate, usdAmount, targetCurrency, exchangeRate, targetAmount, requestedDate);
  }

}
