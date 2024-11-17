package com.moin.demomoin.util.crypto;

import static org.junit.jupiter.api.Assertions.*;

import java.security.NoSuchAlgorithmException;
import javax.crypto.NoSuchPaddingException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MoinCryptoHelperTest {

  @Test
  @DisplayName("기본 키로 암/복호화 성공 테스트")
  void testShouldSuccessToLoadDefaultSecretKeyFromFile()
      throws NoSuchPaddingException, NoSuchAlgorithmException {
    // When
    var secretKey = MoinCryptoHelper.createCrypto();

    // Then
    assertNotNull(secretKey);

    var encrypt = MoinCryptoHelper.encrypt("moin-demo");
    assertEquals("moin-demo", MoinCryptoHelper.decrypt(encrypt));
  }
}