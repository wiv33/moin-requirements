package com.moin.demomoin.util.crypto;

public interface MoinContentCrypto {

  String encrypt(String plain);

  String decrypt(String cipher);
}
