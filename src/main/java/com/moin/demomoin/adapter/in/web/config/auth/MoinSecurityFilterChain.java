package com.moin.demomoin.adapter.in.web.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity.CsrfSpec;
import org.springframework.security.config.web.server.ServerHttpSecurity.FormLoginSpec;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.web.server.SecurityWebFilterChain;
import reactor.core.scheduler.Schedulers;

@Configuration
@EnableWebFluxSecurity
public class MoinSecurityFilterChain {

  @Bean
  SecurityWebFilterChain securityFilterChainClient(ServerHttpSecurity http,
      ReactiveUserDetailsService userDetailsService) {
    return http
        .authorizeExchange(exchanges -> exchanges.pathMatchers("/user/login").permitAll()
            .anyExchange().authenticated())
        .csrf(CsrfSpec::disable)
        .formLogin(FormLoginSpec::disable)
        .addFilterBefore((exchange, chain) -> {

          return chain.filter(exchange);
        }, SecurityWebFiltersOrder.AUTHENTICATION)
        .build();
  }
}
