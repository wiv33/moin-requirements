package com.moin.demomoin.application.port.out;

import com.moin.demomoin.adapter.in.web.config.auth.CurrentUserAuthToken;
import com.moin.demomoin.adapter.out.persistence.entity.MoinUser;
import com.moin.demomoin.application.port.in.LoginCommand;
import com.moin.demomoin.application.port.in.SignupCommand;
import reactor.core.publisher.Mono;

public interface LoginPort {

  Mono<Boolean> existsByUserId(String userId);
  Mono<MoinUser> signup(SignupCommand command);
  Mono<String> login(LoginCommand command);
}
