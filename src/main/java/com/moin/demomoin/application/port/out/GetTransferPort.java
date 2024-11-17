package com.moin.demomoin.application.port.out;

import com.moin.demomoin.domain.dto.MoinTransferDto.ListResponse;
import reactor.core.publisher.Mono;

public interface GetTransferPort {

  Mono<ListResponse> getTransferList(String userId, String username);
}
