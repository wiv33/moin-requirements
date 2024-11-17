package com.moin.demomoin.adapter.in.web.config.auth;

import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.core.Authentication;
import reactor.core.publisher.Mono;

public class BearerTokenAuthenticationManager implements ReactiveAuthenticationManager {

  @Override
  public Mono<Authentication> authenticate(Authentication authentication) {
    return Mono.just(authentication)
        .log("in BearerTokenAuthenticationManager");
  }
}
