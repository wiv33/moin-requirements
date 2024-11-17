package com.moin.demomoin.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum MoinIdType {
  REG_NO("REG_NO", "개인"),
  BUSINESS_NO("BUSINESS_NO", "법인"),
  ;

  private final String code;
  private final String description;
}
