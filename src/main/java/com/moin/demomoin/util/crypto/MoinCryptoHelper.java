package com.moin.demomoin.util.crypto;

import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@UtilityClass
public class MoinAesGcmCryptoHelper {

  public static MoinCryptoImpl createMoinGcmCrypto()
      throws NoSuchPaddingException, NoSuchAlgorithmException {
    return new MoinCryptoImpl();
  }

  public static String decrypt(String plainText, SecretKeySpec secretKey) {
    try {
      return new MoinCryptoImpl().decryptWithIv(Base64.getDecoder().decode(plainText), secretKey);
    } catch (GeneralSecurityException | UnsupportedEncodingException e) {
      log.error("decrypt exception: plainText={}, key={}", plainText, secretKey, e);
      throw new RuntimeException(e);
    }
  }

  public static String encrypt(String plainText, SecretKeySpec secretKey) {
    if (plainText == null || plainText.isEmpty()) {
      plainText = "";
    }
    try {
      return Base64.getEncoder().encodeToString(
          createMoinGcmCrypto().encryptWithIv(plainText.getBytes(MoinCrypto.UTF_8), secretKey, getIv()));
    } catch (GeneralSecurityException | UnsupportedEncodingException e) {
      log.error("encrypt exception", e);
      throw new RuntimeException(e);
    }
  }

  static byte[] getIv() {
    byte[] iv = new byte[MoinCrypto.GCM_IV_LENGTH];
    new SecureRandom().nextBytes(iv);
    return iv;
  }

}
