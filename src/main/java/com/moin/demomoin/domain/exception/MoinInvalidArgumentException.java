package com.moin.demomoin.domain.exception;

import com.moin.demomoin.domain.MoinStatusCode;

public class MoinInvalidArgumentException extends MoinCommonMessageException {

  public MoinInvalidArgumentException(String message) {
    super(message);
  }

  public MoinInvalidArgumentException(MoinStatusCode message) {
    super(message.getCode());
  }

  public MoinInvalidArgumentException(MoinStatusCode message, Object... args) {
    super(message.getCode(), args);
  }

  public MoinInvalidArgumentException(MoinStatusCode message, Throwable cause, Object... args) {
    super(message.getCode(), cause, args);
  }

}
