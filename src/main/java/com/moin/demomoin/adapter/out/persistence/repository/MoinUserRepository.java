package com.moin.demomoin.adapter.out.persistence.repository;

import com.moin.demomoin.adapter.out.persistence.entity.MoinUser;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface MoinUserRepository extends ReactiveCrudRepository<MoinUser, Long> {

  Mono<Boolean> existsByUserId(String userId);

}
