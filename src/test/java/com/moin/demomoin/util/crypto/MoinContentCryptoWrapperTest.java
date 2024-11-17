package com.moin.demomoin.util.crypto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import javax.crypto.spec.SecretKeySpec;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

@DisplayName("암/복호화 테스트")
class MoinContentCryptoWrapperTest {

  static MoinContentCryptoWrapper cryptoWrapper;
  static String key;
  private static SecretKeySpec secretKey;


  @BeforeAll
  static void beforeAll() {
    key = "Ruc/=#(*mNL1?f+v$C&9a#vSmbKa%)X<";
    secretKey = new SecretKeySpec(key.getBytes(), "AES");
    cryptoWrapper = new MoinContentCryptoWrapper(secretKey);
  }

  @DisplayName("암호화 성공 테스트")
  @ParameterizedTest
  @ValueSource(strings = {"moin-demo", "alpha", "zoo", "apache", "spark", "flink",
      "나랏말싸미 듕귁에 달아 문자와로 서르 사맛디 아니할쎄"})
  void testShouldSuccessEncrypt(String plainText) {
    assertEquals(secretKey.getEncoded().length, 32);

    // When
    String encrypted = cryptoWrapper.encrypt(plainText);
    String encrypted2 = cryptoWrapper.encrypt(plainText);

    System.out.println("encrypted = " + encrypted);
    System.out.println("encrypted2 = " + encrypted2);

    // Then
    assertNotEquals(encrypted, encrypted2);
    assertEquals(plainText, cryptoWrapper.decrypt(encrypted));
  }

  @Test
  @DisplayName("복호화 성공 테스트")
  void testShouldSuccessDecrypt() {
    var enc1 = "Hsd5cE81cbn84k/ZDGyBWmkPuwTV/2mH41/wDDzB6GJ+ZgUcYw==";
    var enc2 = "xC/B1wUSJNMpw8yzrjZ9CVHYp+wu3ZgzIbcHwF3uiHI8qUXPOg==";

    assertEquals("moin-demo", cryptoWrapper.decrypt(enc1));
    assertEquals(cryptoWrapper.decrypt(enc1), cryptoWrapper.decrypt(enc2));
  }

  @Test
  @DisplayName("key 변경 후 복호화 실패 테스트")
  void testShouldFailureDecryptTagMismatch() {
    var newKey = key.replace("$", "+");
    var secretKey2 = new SecretKeySpec(newKey.getBytes(), "AES");
    var cryptoWrapper2 = new MoinContentCryptoWrapper(secretKey2);
    var enc1 = "Hsd5cE81cbn84k/ZDGyBWmkPuwTV/2mH41/wDDzB6GJ+ZgUcYw==";

    assertThrows(RuntimeException.class, () -> cryptoWrapper2.decrypt(enc1));
  }
}