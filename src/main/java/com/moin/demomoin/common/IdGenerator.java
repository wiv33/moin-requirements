package com.moin.demomoin.common;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import org.springframework.util.Assert;

public class IdGenerator {

  static final long BASE_MILLIS = 1291033700000L;
  static final long MAX_SEQUENCE = 12L;
  static final int MAX_RANDOM = 0x8700000;

  public static long generate() {
    var t = System.currentTimeMillis() - BASE_MILLIS;
    try {
      return (t << MAX_SEQUENCE) + SecureRandom.getInstanceStrong().nextInt(MAX_RANDOM);
    } catch (NoSuchAlgorithmException e) {
      throw new RuntimeException(e);
    }
  }


  public static void main(String[] args) {
    System.out.println(generate());
    System.out.println(generate());
    System.out.println(generate());
    System.out.println(generate());
    System.out.println(generate());
    System.out.println(generate());
    System.out.println(generate());
    System.out.println(generate());
    System.out.println(generate());
    System.out.println(generate());
    System.out.println(generate());
    System.out.println(generate());
    Assert.isTrue(String.valueOf(generate()).length() == 16, "Id length must be 16.");
  }
}
