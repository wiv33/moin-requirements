package com.moin.demomoin.application.handler;

import static org.springframework.web.reactive.function.server.ServerResponse.ok;

import com.moin.demomoin.adapter.in.web.config.auth.CurrentUserAuthToken;
import com.moin.demomoin.application.port.in.LoginCommand;
import com.moin.demomoin.application.port.in.SignupCommand;
import com.moin.demomoin.application.port.out.UserPort;
import com.moin.demomoin.domain.MoinResponse;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Service
public class LoginHandler {

  private final UserPort userPort;

  public Mono<ServerResponse> signup(ServerRequest request) {

    return request.bodyToMono(SignupCommand.class)
        .flatMap(userPort::signup)
        .then(Mono.defer(() -> ok().bodyValue(MoinResponse.ok())));
  }

  public Mono<ServerResponse> login(ServerRequest request) {
    return request.bodyToMono(LoginCommand.class)
        .flatMap(userPort::login)
        .flatMap(token -> ok()
            .header("JWT", token)
            .bodyValue(MoinResponse.mergeBody(Map.of("JWT", token))))
        .switchIfEmpty(Mono.defer(() -> ok().bodyValue(MoinResponse.unauthorized())));
  }

  /**
   * for test
   *
   * @param serverRequest
   * @return
   */
  public Mono<ServerResponse> delete(ServerRequest serverRequest) {
    return userPort.delete(serverRequest.pathVariable("userId"))
        .then(Mono.defer(() -> ok().bodyValue(MoinResponse.ok())));
  }

  /**
   * for test
   *
   * @param serverRequest
   * @return
   */
  public Mono<ServerResponse> get(ServerRequest serverRequest) {
    return serverRequest.principal()
        .cast(CurrentUserAuthToken.class)
        .flatMap(principal -> userPort.get(principal.getUserId())
            .flatMap(user -> ok().bodyValue(MoinResponse.mergeBody(Map.of("moinUser", user))))
            .switchIfEmpty(Mono.defer(() -> ok().bodyValue(MoinResponse.ok()))));
  }
}
