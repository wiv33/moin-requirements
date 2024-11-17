package com.moin.demomoin.domain.exception;

import com.moin.demomoin.domain.MoinStatusCode;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.FORBIDDEN, value = HttpStatus.FORBIDDEN)
public class MoinUnauthorizedException extends MoinCommonMessageException {

  public MoinUnauthorizedException(MoinStatusCode message) {
    super(message.getCode());
  }

  public MoinUnauthorizedException(MoinStatusCode message, Object... args) {
    super(message.getCode(), args);
  }

  public MoinUnauthorizedException(MoinStatusCode message, Throwable cause, Object... args) {
    super(message.getCode(), cause, args);
  }
}
