package com.moin.demomoin.domain;

import static com.moin.demomoin.domain.MoinCommissionFactory.JPY_OVER_1;
import static com.moin.demomoin.domain.MoinCommissionFactory.USD_OVER_100;
import static com.moin.demomoin.domain.MoinCommissionFactory.USD_UP_TO_100;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

// todo DB 관리 여부 체크
@DisplayName("수수료 테스트")
class MoinCommissionFactoryTest {

  @Test
  @DisplayName("USD 100만 원 이하 수수료 테스트 - 1000, 0.2%")
  void testShouldGetUsdUpTo100Commission() {
    BigDecimal sourceAmount = BigDecimal.valueOf(1_000_000);

    MoinCommissionFactory commission = MoinCommissionFactory.getCommission(MoinCurrencyType.USD,
        sourceAmount);
    var name = commission.name();
    assertEquals(USD_UP_TO_100.name(), name);
  }

  @Test
  @DisplayName("100만 원 초과 수수료 테스트 - 3000, 0.1%")
  void testShouldGetUsdOver100Commission() {
    BigDecimal sourceAmount = BigDecimal.valueOf(1_001_000);

    MoinCommissionFactory commission = MoinCommissionFactory.getCommission(MoinCurrencyType.USD,
        sourceAmount);
    var name = commission.name();
    assertEquals(USD_OVER_100.name(), name);
  }


  @Test
  @DisplayName("JPY 1만 원 초과 수수료 테스트 - 3000, 0.5%")
  void testShouldGetJpyOver1Commission() {
    BigDecimal sourceAmount = BigDecimal.valueOf(1_000_000);
    MoinCommissionFactory commission = MoinCommissionFactory.getCommission(MoinCurrencyType.JPY,
        sourceAmount);
    var name = commission.name();
    assertEquals(JPY_OVER_1.name(), name);

    BigDecimal sourceAmount2 = BigDecimal.valueOf(5_000_000);
    MoinCommissionFactory commission2 = MoinCommissionFactory.getCommission(MoinCurrencyType.JPY,
        sourceAmount2);
    var name2 = commission2.name();
    assertEquals(JPY_OVER_1.name(), name2);
  }


}