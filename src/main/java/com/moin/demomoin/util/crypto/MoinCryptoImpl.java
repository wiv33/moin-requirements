package com.moin.demomoin.util.crypto;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.GCMParameterSpec;
import org.springframework.util.Assert;

public class MoinCryptoImpl implements MoinCrypto {

  private final Cipher cipher;

  MoinCryptoImpl() throws NoSuchPaddingException, NoSuchAlgorithmException {
    this.cipher = Cipher.getInstance(AES_GCM_NO_PADDING);
  }

  public byte[] encryptWithIv(byte[] plainText, Key key, byte[] iv)
      throws GeneralSecurityException {
    byte[] encryptedText = encrypt(plainText, key, iv);
    return ByteBuffer.allocate(iv.length + encryptedText.length)
        .put(iv)
        .put(encryptedText)
        .array();
  }

  public String decryptWithIv(byte[] cipherText, Key key)
      throws GeneralSecurityException, UnsupportedEncodingException {
    ByteBuffer bb = ByteBuffer.wrap(cipherText);

    byte[] iv = new byte[GCM_IV_LENGTH];
    bb.get(iv);

    byte[] cText = new byte[bb.remaining()];
    bb.get(cText);

    Assert.isTrue(bb.remaining() == 0, "Invalid cipherText");
    return new String(decrypt(cText, key, iv), StandardCharsets.UTF_8);
  }

  @Override
  public byte[] encrypt(byte[] plainText, Key key, byte[] iv) throws GeneralSecurityException {
    cipher.init(Cipher.ENCRYPT_MODE, key, new GCMParameterSpec(GCM_TAG_BIT, iv));
    return cipher.doFinal(plainText);
  }

  @Override
  public byte[] decrypt(byte[] cipherText, Key key, byte[] iv) throws GeneralSecurityException {
    cipher.init(Cipher.DECRYPT_MODE, key, new GCMParameterSpec(GCM_TAG_BIT, iv));
    return cipher.doFinal(cipherText);
  }

}
