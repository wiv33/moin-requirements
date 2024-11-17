package com.moin.demomoin.adapter.out.persistence;

import static org.springframework.data.relational.core.query.Criteria.where;
import static org.springframework.data.relational.core.query.Query.query;

import com.moin.demomoin.adapter.out.persistence.entity.MoinIdType;
import com.moin.demomoin.adapter.out.persistence.entity.MoinTransferQuote;
import com.moin.demomoin.adapter.out.persistence.entity.MoinTransferRequest;
import com.moin.demomoin.adapter.out.persistence.repository.MoinTransferRequestRepository;
import com.moin.demomoin.application.port.in.TransferQuoteCommand;
import com.moin.demomoin.application.port.in.TransferRequestCommand;
import com.moin.demomoin.application.port.out.GetTransferPort;
import com.moin.demomoin.application.port.out.SaveTransferPort;
import com.moin.demomoin.common.PersistenceAdapter;
import com.moin.demomoin.domain.MoinCurrencyType;
import com.moin.demomoin.domain.MoinStatusCode;
import com.moin.demomoin.domain.MoinTransferCalculationUnit;
import com.moin.demomoin.domain.dto.MoinTransferDto.ListResponse;
import com.moin.demomoin.domain.dto.MoinTransferDto.QuoteResponse;
import com.moin.demomoin.domain.exception.MoinInvalidArgumentException;
import com.moin.demomoin.util.ExchangeInfoApi;
import com.moin.demomoin.util.InstantUtil;
import java.time.Instant;
import lombok.RequiredArgsConstructor;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@PersistenceAdapter
@RequiredArgsConstructor
public class TransferAdapter implements GetTransferPort, SaveTransferPort {

  private final MoinTransferRequestRepository repository;
  private final R2dbcEntityTemplate template;

  @Override
  public Mono<Void> saveTransferRequest(String userId, String username, MoinIdType idType,
      TransferRequestCommand command) {

    return repository.sumTotalUsdAmount(
            userId, InstantUtil.toStartTimestamp(), InstantUtil.toEndTimestamp(), command.quoteId())
        .log("total sum ->>>>>>>>>>>>>>>>>>")
        .flatMap(total -> {
          if (idType.isOverMaximumTransferDollar(total)) {
            return Flux.error(new MoinInvalidArgumentException(MoinStatusCode.LIMIT_EXCESS));
          }
          return Flux.empty();
        })
        .switchIfEmpty(
            template.exists(query(where("quote_id").is(command.quoteId())),
                    MoinTransferRequest.class)
                .flatMapMany(exists -> {
                  if (exists) {
                    return Flux.error(new MoinInvalidArgumentException(
                        MoinStatusCode.INVALID_ALREADY_USED_QUOTE));
                  }
                  return template.selectOne(query(where("id").is(command.quoteId().toString())),
                      MoinTransferQuote.class);
                })
                .map(MoinTransferQuote::validateThenToTransferRequest)
                .flatMap(template::insert)
        )
        .log("inserted transfer request")
        .then()
        ;
  }

  @Override
  public Mono<QuoteResponse> saveTransferQuote(String userId, TransferQuoteCommand command) {
    return ExchangeInfoApi.getExchangeRate(MoinCurrencyType.USD, command.targetCurrency())
        .map(unitMap -> new MoinTransferCalculationUnit(command, unitMap))
        .flatMap(unit -> template.insert(MoinTransferQuote.class)
            .using(new MoinTransferQuote(userId, Instant.now(), unit)))
        .map(MoinTransferQuote::toResponse);
  }

  @Override
  public Mono<ListResponse> getTransferList(String userId, String username) {
    return template.select(MoinTransferRequest.class)
        .matching(query(where("userId").is(userId)))
        .all()
        .map(MoinTransferRequest::toResponse)
        .buffer()
        .map(s -> new ListResponse(userId, username, s))
        .single()
        ;
  }
}
