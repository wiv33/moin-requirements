package com.moin.demomoin.adapter.in.web.config;

import java.util.function.Function;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class ServerHttpBearerAuthenticationConverter implements
    Function<ServerWebExchange, Mono<Authentication>> {

  private static final String BEARER = "Bearer ";

  private final JwtVerifierImpl jwtVerifier;

  @Override
  public Mono<Authentication> apply(ServerWebExchange serverWebExchange) {
    return Mono.justOrEmpty(serverWebExchange)
        .mapNotNull(exchange -> exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION))
        .filter(token -> token.length() > BEARER.length())
        .map(token -> token.substring(BEARER.length()))
        .flatMap(jwtVerifier::check)
        .map(CurrentUserAuthToken::new);
  }
}
