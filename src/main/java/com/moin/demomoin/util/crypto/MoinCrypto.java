package com.moin.demomoin.util.crypto;

import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.Key;

public interface MoinCrypto {

  String AES_GCM_NO_PADDING = "AES/GCM/NoPadding";
  String UTF_8 = StandardCharsets.UTF_8.name();
  int GCM_IV_LENGTH = 12;
  int GCM_TAG_BIT = 16 * 8;

  byte[] encrypt(byte[] plainText, Key key, byte[] iv) throws GeneralSecurityException;

  byte[] decrypt(byte[] cipherText, Key key, byte[] iv) throws GeneralSecurityException;

}
