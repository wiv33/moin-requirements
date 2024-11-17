package com.moin.demomoin.domain.exception;

public class MoinUnauthorized extends MoinCommonMessage {

  public MoinUnauthorized(MoinStatusCode message) {
    super(message.getCode());
  }

  public MoinUnauthorized(MoinStatusCode message, Object... args) {
    super(message.getCode(), args);
  }

  public MoinUnauthorized(MoinStatusCode message, Throwable cause, Object... args) {
    super(message.getCode(), cause, args);
  }
}
