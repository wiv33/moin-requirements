package com.moin.demomoin.adapter.in.web;

import static org.springframework.web.reactive.function.server.RequestPredicates.accept;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

import com.moin.demomoin.application.handler.TransferHandler;
import com.moin.demomoin.common.WebAdapter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;

@RequiredArgsConstructor
@WebAdapter
public class TransferRoute {

  private final TransferHandler handler;

  @Bean
  RouterFunction<?> transferRoutes() {
    return route()
        .POST("/transfer/quote",accept(MediaType.APPLICATION_JSON), handler::quote)
        .POST("/transfer/request",accept(MediaType.APPLICATION_JSON), handler::request)
        .GET("/transfer/list", accept(MediaType.APPLICATION_JSON), handler::requestList)
        .build()
        ;
  }
}
