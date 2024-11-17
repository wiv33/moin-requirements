package com.moin.demomoin.adapter.out.persistence;

import static org.springframework.data.relational.core.query.Criteria.where;
import static org.springframework.data.relational.core.query.Query.query;

import com.moin.demomoin.adapter.in.web.config.auth.JwtSignComponent;
import com.moin.demomoin.adapter.out.persistence.entity.MoinUser;
import com.moin.demomoin.application.port.in.LoginCommand;
import com.moin.demomoin.application.port.in.SignupCommand;
import com.moin.demomoin.application.port.out.UserPort;
import com.moin.demomoin.common.PersistenceAdapter;
import com.moin.demomoin.domain.exception.MoinNotFoundException;
import com.moin.demomoin.domain.MoinStatusCode;
import com.moin.demomoin.domain.exception.MoinUserAlreadyExistsException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.util.Assert;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@PersistenceAdapter
public class LoginAdapter implements UserPort {

  private final R2dbcEntityTemplate template;
  private final JwtSignComponent jwtSignComponent;

  @Override
  public Mono<Boolean> existsByUserId(String userId) {
    Assert.notNull(userId, "userId must not be null");
    Assert.notNull(template, "template must not be null");

    return template.exists(query(where("userId").is(userId)), MoinUser.class)
        .log()
        ;
  }

  @Override
  public Mono<MoinUser> signup(SignupCommand command) {
    return existsByUserId(command.userId())
        .log()
        .flatMap(exists -> {
          if (exists) {
            return Mono.error(
                new MoinUserAlreadyExistsException(MoinStatusCode.USER_ALREADY_EXISTS));
          }
          var entity = new MoinUser(command);
          return template.insert(entity)
              .log()
              ;
        });
  }

  @Override
  public Mono<String> login(LoginCommand command) {
    return template.select(query(where("userId").is(command.userId())), MoinUser.class)
        .singleOrEmpty()
        .log()
        .switchIfEmpty(Mono.error(new MoinNotFoundException(MoinStatusCode.USER_NOT_FOUND)))
        .flatMap(user -> user.matchPassword(command.password()) ?
            Mono.just(jwtSignComponent.createToken(user)) : Mono.empty())
        ;
  }

  public Mono<Long> delete(String userId) {
    return template.delete(query(where("userId").is(userId)), MoinUser.class)
        .log()
        ;
  }

  public Mono<MoinUser> get(String userId) {
    return template.select(query(where("userId").is(userId)), MoinUser.class)
        .log()
        .singleOrEmpty()
        ;
  }
}
