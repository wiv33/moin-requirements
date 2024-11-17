package com.moin.demomoin.adapter.in.web.config.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class ServerHttpBearerAuthenticationConverter implements ServerAuthenticationConverter {

  private static final String BEARER_PREFIX = "Bearer ";

  @Override
  public Mono<Authentication> convert(ServerWebExchange exchange) {
    return Mono.justOrEmpty(exchange)
        .mapNotNull(ex -> ex.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION))
        .filter(token -> token.length() > BEARER_PREFIX.length())
        .map(token -> token.substring(BEARER_PREFIX.length()))
        .flatMap(JwtVerifier::check)
        .map(CurrentUserAuthToken::new)
  ;
  }
}
