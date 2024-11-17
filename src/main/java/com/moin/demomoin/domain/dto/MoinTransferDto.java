package com.moin.demomoin.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.moin.demomoin.domain.MoinCurrencyType;
import com.moin.demomoin.util.InstantUtil;
import java.beans.Transient;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import lombok.Getter;
import lombok.experimental.UtilityClass;

@UtilityClass
public class MoinTransferDto {

  public record QuoteResponse(
      Long id,
      BigDecimal exchangeRate,
      LocalDateTime expireTime,
      BigDecimal targetAmount
  ) {

    public QuoteResponse(Long id, BigDecimal exchangeRate, Instant expireTime,
        BigDecimal targetAmount) {
      this(id, exchangeRate, InstantUtil.toLocalDateTime(expireTime), targetAmount);
    }
  }

  public record RequestResponse(
      BigDecimal sourceAmount,
      BigDecimal fee,
      BigDecimal usdExchangeRate,
      BigDecimal usdAmount,
      MoinCurrencyType targetCurrency,
      BigDecimal exchangeRate,
      BigDecimal targetAmount,
      @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
      LocalDateTime requestedDate,
      String feeRate
  ) {

    @Transient
    public boolean isToday() {
      return requestedDate.toLocalDate().equals(LocalDateTime.now().toLocalDate());
    }
  }

  @Getter
  public static final class ListResponse {

    private final String userId;
    private final String name;
    private final int todayTransferCount;
    private final BigDecimal todayTransferUsdAmount;
    private final List<RequestResponse> history;

    public ListResponse(
        String userId,
        String name,
        List<RequestResponse> history
    ) {
      this.userId = userId;
      this.name = name;
      this.history = history;
      this.todayTransferCount = history.stream().filter(RequestResponse::isToday).toList().size();
      this.todayTransferUsdAmount = history.stream()
          .filter(RequestResponse::isToday)
          .map(RequestResponse::usdAmount)
          .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public String userId() {
      return userId;
    }

    public String name() {
      return name;
    }

    public int todayTransferCount() {
      return todayTransferCount;
    }

    public BigDecimal todayTransferUsdAmount() {
      return todayTransferUsdAmount;
    }

    public List<RequestResponse> history() {
      return history;
    }

    @Override
    public boolean equals(Object obj) {
      if (obj == this) {
        return true;
      }
      if (obj == null || obj.getClass() != this.getClass()) {
        return false;
      }
      var that = (ListResponse) obj;
      return Objects.equals(this.userId, that.userId) &&
          Objects.equals(this.name, that.name) &&
          this.todayTransferCount == that.todayTransferCount &&
          Objects.equals(this.todayTransferUsdAmount, that.todayTransferUsdAmount) &&
          Objects.equals(this.history, that.history);
    }

    @Override
    public int hashCode() {
      return Objects.hash(userId, name, todayTransferCount, todayTransferUsdAmount, history);
    }

    @Override
    public String toString() {
      return "ListResponse[" +
          "userId=" + userId + ", " +
          "name=" + name + ", " +
          "todayTransferCount=" + todayTransferCount + ", " +
          "todayTransferUsdAmount=" + todayTransferUsdAmount + ", " +
          "history=" + history + ']';
    }

    public Map<String, Object> toMap() {
      var result = new LinkedHashMap<String, Object>();
      result.put("userId", userId);
      result.put("name", name);
      result.put("todayTransferCount", todayTransferCount);
      result.put("todayTransferUsdAmount", todayTransferUsdAmount);
      result.put("history", history);
      return result;
    }
  }

}
