package com.moin.demomoin.application.handler;

import static org.springframework.web.reactive.function.server.ServerResponse.ok;

import com.moin.demomoin.adapter.in.web.config.auth.CurrentUserAuthToken;
import com.moin.demomoin.application.port.in.TransferQuoteCommand;
import com.moin.demomoin.application.port.in.TransferRequestCommand;
import com.moin.demomoin.application.port.out.GetTransferPort;
import com.moin.demomoin.application.port.out.SaveTransferPort;
import com.moin.demomoin.domain.MoinResponse;
import com.moin.demomoin.domain.dto.MoinTransferDto.ListResponse;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Service
public class TransferHandler {

  private final SaveTransferPort saveTransferPort;
  private final GetTransferPort getTransferPort;

  public Mono<ServerResponse> quote(ServerRequest request) {
    return Mono.zip(request.principal().cast(CurrentUserAuthToken.class),
            request.bodyToMono(TransferQuoteCommand.class))
        .flatMap(
            tuple -> saveTransferPort.saveTransferQuote(tuple.getT1().getUserId(), tuple.getT2()))
        .flatMap(response -> ok().bodyValue(MoinResponse.mergeBody(Map.of("quote", response))));
  }

  public Mono<ServerResponse> request(ServerRequest request) {
    return ok().body(
        BodyInserters.fromProducer(Mono.zip(request.principal().cast(CurrentUserAuthToken.class),
                request.bodyToMono(TransferRequestCommand.class))
            .flatMap(tuple -> saveTransferPort.saveTransferRequest(tuple.getT1().getUserId(),
                tuple.getT1().getName(),
                tuple.getT1().getIdType(), tuple.getT2()))
            .thenReturn(MoinResponse.ok()), MoinResponse.class));
  }

  public Mono<ServerResponse> requestList(ServerRequest request) {
    return request.principal().cast(CurrentUserAuthToken.class)
        .flatMap(auth -> ok().body(
            getTransferPort.getTransferList(auth.getUserId(), auth.getName())
                .map(ListResponse::toMap)
                .map(MoinResponse::mergeBody),
            Map.class));
  }
}
