package com.moin.demomoin.adapter.in.web.config.auth;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity.CsrfSpec;
import org.springframework.security.config.web.server.ServerHttpSecurity.FormLoginSpec;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatchers;

@Configuration
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
public class MoinSecurityFilterChain {

  @Bean
  SecurityWebFilterChain securityFilterChainClient(
      ServerHttpSecurity http) {

    return http
        .formLogin(FormLoginSpec::disable)
        .csrf(CsrfSpec::disable)
        .authorizeExchange(
            exchanges -> exchanges.pathMatchers("/user/login", "/user/signup").permitAll()
                .anyExchange().authenticated())
        .addFilterBefore((exchange, chain) -> {
          var filter = new AuthenticationWebFilter(new BearerTokenAuthenticationManager());
          filter.setServerAuthenticationConverter(new ServerHttpBearerAuthenticationConverter());
          filter.setAuthenticationFailureHandler(new BearerTokenAuthenticationFailureHandler());
//          filter.setAuthenticationSuccessHandler(new BearerTokenAuthenticationSuccessHandler());
          filter.setRequiresAuthenticationMatcher(ServerWebExchangeMatchers.pathMatchers("/**"));
          return filter.filter(exchange, chain);
        }, SecurityWebFiltersOrder.AUTHENTICATION)
        .build();
  }
}
