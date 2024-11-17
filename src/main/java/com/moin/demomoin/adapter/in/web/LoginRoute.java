package com.moin.demomoin.adapter.in.web;

import static org.springframework.web.reactive.function.server.RouterFunctions.route;

import com.moin.demomoin.application.handler.LoginHandler;
import com.moin.demomoin.common.WebAdapter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.server.RouterFunction;

@RequiredArgsConstructor
@WebAdapter
public class LoginRoute {

  private final LoginHandler loginHandler;

  @Bean
  RouterFunction<?> loginRoutes() {
    return route()
        .POST("/user/signup", loginHandler::signup)
        .POST("/user/login", loginHandler::login)
        .GET("/user/{userId}", loginHandler::get)
        .DELETE("/user/{userId}", loginHandler::delete)
        .build();
  }
}
