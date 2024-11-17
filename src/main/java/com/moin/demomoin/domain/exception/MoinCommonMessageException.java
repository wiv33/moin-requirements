package com.moin.demomoin.domain.exception;

import java.io.Serial;

public class MoinCommonMessage extends RuntimeException {

  private final transient Object[] args;

  @Serial
  private static final long serialVersionUID = 6253788971128545797L;

  public MoinCommonMessage(String message) {
    super(message);
    this.args = null;
  }

  public MoinCommonMessage(String message, Object... args) {
    super(message);
    this.args = args;
  }

  public MoinCommonMessage(String message, Throwable cause, Object... args) {
    super(message, cause);
    this.args = args;
  }

}
