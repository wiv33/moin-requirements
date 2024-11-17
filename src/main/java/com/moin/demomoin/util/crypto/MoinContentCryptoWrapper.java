package com.moin.demomoin.util.crypto;

import javax.crypto.spec.SecretKeySpec;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class MoinContentCryptoWrapper implements MoinContentCrypto {

  private final SecretKeySpec secretKey;

  /**
   * Encrypts the given plain text.
   * 암호화
   *
   * @param plainText the plain text to encrypt
   * @return the encrypted text
   */
  @Override
  public String encrypt(String plainText) {
    return MoinCryptoHelper.encrypt(plainText, secretKey);
  }

  /**
   * Decrypts the given cipher text.
   * 복호화
   *
   * @param cipherText the cipher text to decrypt
   * @return the decrypted text
   */
  @Override
  public String decrypt(String cipherText) {
    return MoinCryptoHelper.decrypt(cipherText, secretKey);
  }
}
