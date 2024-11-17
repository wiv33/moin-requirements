package com.moin.demomoin.adapter.in.web;

import com.moin.demomoin.adapter.in.web.config.auth.JwtSignComponent;
import com.moin.demomoin.adapter.in.web.config.auth.MoinSecurityFilterChain;
import org.springframework.context.annotation.Import;

@Import(MoinSecurityFilterChain.class)
class RouteBaseTest {

  protected static String createToken() {
    return "Bearer %s".formatted(new JwtSignComponent().createToken("moin@themoin.com"));
  }

}
