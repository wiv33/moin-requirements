package com.moin.demomoin.util.crypto;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.GeneralSecurityException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.stream.Stream;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;
import org.springframework.util.ResourceUtils;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Flux;

/**
 * 암/복호화 wrapper class
 */
@Slf4j
@UtilityClass
public class MoinCryptoHelper {

  private static final SecureRandom SECURE_RANDOM = new SecureRandom();
  private static SecretKeySpec DEFAULT_SECRET_KEY;

  static {
    // default secret key text given from file
    try {
      var file = ResourceUtils.getFile("classpath:secretKey");
      Flux.using(() -> Files.lines(file.toPath()), Flux::fromStream, Stream::close)
          .doOnNext(txt -> DEFAULT_SECRET_KEY = new SecretKeySpec(txt.getBytes(), "AES"))
          .subscribe();
    } catch (FileNotFoundException e) {
      throw new RuntimeException(e);
    }

  }

  public static MoinCryptoImpl createCrypto()
      throws NoSuchPaddingException, NoSuchAlgorithmException {
    return new MoinCryptoImpl();
  }

  public static String decrypt(String plainText) {
    return decrypt(plainText, DEFAULT_SECRET_KEY);
  }

  public static String encrypt(String plainText) {
    return encrypt(plainText, DEFAULT_SECRET_KEY);
  }

  /**
   * Decrypts the given plain text using the given secret key. plainText를 복호화합니다.
   *
   * @param plainText the plain text to decrypt
   * @param secretKey the secret key to use for decryption
   * @return the decrypted plain text
   */
  public static String decrypt(String plainText, SecretKeySpec secretKey) {
    try {
      return new MoinCryptoImpl().decryptWithIv(Base64.getDecoder().decode(plainText), secretKey);
    } catch (GeneralSecurityException | UnsupportedEncodingException e) {
      log.error("decrypt exception: plainText={}, key={}", plainText, secretKey, e);
      throw new RuntimeException(e);
    }
  }

  /**
   * Encrypts the given plain text using the given secret key. plainText를 암호화합니다.
   *
   * @param plainText the plain text to encrypt
   * @param secretKey the secret key to use for encryption
   * @return the encrypted plain text
   */
  public static String encrypt(String plainText, SecretKeySpec secretKey) {
    if (!StringUtils.hasText(plainText)) {
      plainText = "";
    }
    try {
      return Base64.getEncoder().encodeToString(
          createCrypto().encryptWithIv(plainText.getBytes(MoinCrypto.UTF_8), secretKey, getIv()));
    } catch (GeneralSecurityException | UnsupportedEncodingException e) {
      log.error("encrypt exception", e);
      throw new RuntimeException(e);
    }
  }

  /**
   * Generates a random initialization vector. 랜덤한 초기화 벡터를 생성합니다.
   *
   * @return the generated initialization vector
   */
  static byte[] getIv() {
    byte[] iv = new byte[MoinCrypto.GCM_IV_LENGTH];
    SECURE_RANDOM.nextBytes(iv);
    return iv;
  }

}
