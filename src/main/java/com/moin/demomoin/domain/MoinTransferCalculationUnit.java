package com.moin.demomoin.domain;

import com.moin.demomoin.application.port.in.TransferQuoteCommand;
import com.moin.demomoin.domain.exception.MoinInvalidArgumentException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Currency;
import java.util.Map;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@RequiredArgsConstructor
@ToString
public class MoinTransferCalculationUnit {

  private final String sourceCurrency = "KRW";
  private final MoinCurrencyType targetCurrency;
  private final BigDecimal sourceAmount;
  private final BigDecimal fixedFee;
  private final BigDecimal feeRate;
  private final MoinCommissionFactory commission;
  private final BigDecimal exchangeRate;
  private final BigDecimal targetAmount;

  // for usd
  private final BigDecimal usdExchangeRate;
  private final BigDecimal usdAmount;

  public MoinTransferCalculationUnit(TransferQuoteCommand command,
      Map<MoinCurrencyType, ExchangeInformation> exchangeInformationMap) {
    this.targetCurrency = command.targetCurrency();
    this.sourceAmount = new BigDecimal(command.amount()).setScale(getDigits(),
        RoundingMode.HALF_UP);
    this.commission = MoinCommissionFactory.getCommission(targetCurrency, sourceAmount);
    this.fixedFee = commission.getFixedFee();
    this.feeRate = commission.getFeeRate();
    this.exchangeRate = exchangeInformationMap.get(targetCurrency).basePrice();
    this.targetAmount = calculateTargetAmount();

    // for usd
    var usdCommission = MoinCommissionFactory.getCommission(MoinCurrencyType.USD, sourceAmount);
    this.usdExchangeRate = exchangeInformationMap.get(MoinCurrencyType.USD).basePrice();
    this.usdAmount = calculateUsdAmount(usdCommission);

    if (targetAmount.compareTo(BigDecimal.ZERO) <= 0) {
      throw new MoinInvalidArgumentException(MoinStatusCode.NEGATIVE_NUMBER);
    }
  }

  private BigDecimal calculateUsdAmount(MoinCommissionFactory usdCommission) {
    return sourceAmount.divide(usdExchangeRate, getDigits(MoinCurrencyType.USD), RoundingMode.HALF_UP)
        .subtract(getCommissionUsdAmount(usdCommission));
  }

  /**
   * usd 수수료 계산
   * 수수료 = (보내는금액(amount) * 수수료율 + 고정수수료) / 환율
   *
   * @param usdCommission usd 수수료
   * @return usd 수수료
   */
  private BigDecimal getCommissionUsdAmount(MoinCommissionFactory usdCommission) {
    return usdCommission.getTotalCommission(sourceAmount)
        .divide(usdExchangeRate, getDigits(MoinCurrencyType.USD), RoundingMode.HALF_UP);
  }

  /**
   * 받는 금액으로 수수료 계산
   * 수수료 = (보내는금액(amount) * 수수료율 + 고정수수료) / 환율
   *
   * @return 수수료
   */
  public BigDecimal getCommissionAmount() {
    return commission.getTotalCommission(sourceAmount)
        .divide(exchangeRate, getDigits(), RoundingMode.HALF_UP);
  }

  /**
   * 받는 금액 = (보내는 금액 - 수수료) / 환율
   *
   * @return 받는 금액
   */
  private BigDecimal calculateTargetAmount() {
    return sourceAmount.divide(exchangeRate, getDigits(), RoundingMode.HALF_UP)
        .subtract(getCommissionAmount());
  }

  /**
   * 기본 자릿수
   *
   * @return int
   */
  private int getDigits() {
    return Currency.getInstance(targetCurrency.name()).getDefaultFractionDigits();
  }

  /**
   * 기본 자릿수
   *
   * @return int
   */
  private int getDigits(MoinCurrencyType currencyType) {
    return Currency.getInstance(currencyType.name()).getDefaultFractionDigits();
  }

  /**
   * client용 string format
   *
   * @return % 단위
   */
  public String getFeeRateText() {
    return feeRate.multiply(BigDecimal.valueOf(100)).setScale(0, RoundingMode.HALF_UP) + "%";
  }

}
