package com.moin.demomoin.application.port.out;

import com.moin.demomoin.adapter.out.persistence.entity.MoinUser;
import com.moin.demomoin.application.port.in.LoginCommand;
import com.moin.demomoin.application.port.in.SignupCommand;
import reactor.core.publisher.Mono;

public interface UserPort {

  Mono<Boolean> existsByUserId(String userId);
  Mono<MoinUser> signup(SignupCommand command);
  Mono<String> login(LoginCommand command);
  Mono<Long> delete(String userId);
  Mono<MoinUser> get(String userId);
}
