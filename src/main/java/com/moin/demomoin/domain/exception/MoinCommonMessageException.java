package com.moin.demomoin.domain.exception;

import com.moin.demomoin.domain.MoinStatusCode;
import java.io.Serial;

public class MoinCommonMessageException extends RuntimeException {

  private final transient Object[] args;

  @Serial
  private static final long serialVersionUID = 6253788971128545797L;

  public MoinCommonMessageException(MoinStatusCode code) {
    super(code.getCode());
    this.args = null;
  }

  public MoinCommonMessageException(String message) {
    super(message);
    this.args = null;
  }

  public MoinCommonMessageException(String message, Object... args) {
    super(message);
    this.args = args;
  }

  public MoinCommonMessageException(String message, Throwable cause, Object... args) {
    super(message, cause);
    this.args = args;
  }

}
