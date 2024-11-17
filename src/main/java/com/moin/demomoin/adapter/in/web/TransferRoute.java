package com.moin.demomoin.adapter.in.web;

import static org.springframework.web.reactive.function.server.RouterFunctions.route;

import com.moin.demomoin.application.handler.QuoteHandler;
import com.moin.demomoin.common.WebAdapter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;

@RequiredArgsConstructor
@WebAdapter
public class QuoteRoute {

  private final QuoteHandler handler;

  @Bean
  RouterFunction<?> quoteRoutes() {
    return route()
        .GET("/", handler::)
  }
}
