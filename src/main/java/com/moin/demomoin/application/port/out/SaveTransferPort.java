package com.moin.demomoin.application.port.out;

import com.moin.demomoin.adapter.out.persistence.entity.MoinIdType;
import com.moin.demomoin.application.port.in.TransferQuoteCommand;
import com.moin.demomoin.application.port.in.TransferRequestCommand;
import com.moin.demomoin.domain.dto.MoinTransferDto;
import reactor.core.publisher.Mono;

public interface SaveTransferPort {

  Mono<MoinTransferDto.QuoteResponse> saveTransferQuote(String userId,
      TransferQuoteCommand command);

  Mono<Void> saveTransferRequest(String userId,
      String username,
      MoinIdType idType,
      TransferRequestCommand command);
}
