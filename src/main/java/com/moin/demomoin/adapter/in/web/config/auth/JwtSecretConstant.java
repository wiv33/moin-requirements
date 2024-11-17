package com.moin.demomoin.adapter.in.web.config.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum JwtSecretConstant {
  DEFAULT_SECRET("moin_default_secret"),
  ;

  private final String code;
}
