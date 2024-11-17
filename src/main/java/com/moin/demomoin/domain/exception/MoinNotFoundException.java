package com.moin.demomoin.domain.exception;

import com.moin.demomoin.domain.MoinStatusCode;

public class MoinNotFoundException extends MoinCommonMessageException{

  public MoinNotFoundException(MoinStatusCode code) {
    super(code);
  }

}
