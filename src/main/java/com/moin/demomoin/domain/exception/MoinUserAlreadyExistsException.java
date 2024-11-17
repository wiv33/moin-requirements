package com.moin.demomoin.domain.exception;

import com.moin.demomoin.domain.MoinStatusCode;

public class MoinUserAlreadyExistsException extends MoinCommonMessageException {

  public MoinUserAlreadyExistsException(String message) {
    super(message);
  }

  public MoinUserAlreadyExistsException(MoinStatusCode message) {
    super(message.getCode());
  }

  public MoinUserAlreadyExistsException(MoinStatusCode message, Object... args) {
    super(message.getCode(), args);
  }

  public MoinUserAlreadyExistsException(MoinStatusCode message, Throwable cause, Object... args) {
    super(message.getCode(), cause, args);
  }
}
