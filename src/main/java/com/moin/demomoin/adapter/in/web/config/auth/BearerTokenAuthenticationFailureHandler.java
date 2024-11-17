package com.moin.demomoin.adapter.in.web.config.auth;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.server.WebFilterExchange;
import org.springframework.security.web.server.authentication.ServerAuthenticationFailureHandler;
import reactor.core.publisher.Mono;

public class BearerTokenAuthenticationFailureHandler implements ServerAuthenticationFailureHandler {

  @Override
  public Mono<Void> onAuthenticationFailure(WebFilterExchange webFilterExchange,
      AuthenticationException exception) {
    var response = webFilterExchange.getExchange().getResponse();
    response.setStatusCode(HttpStatus.UNAUTHORIZED);
    response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
    return response.writeWith(
        Mono.just(response.bufferFactory().wrap(exception.getMessage().getBytes())));
  }
}
