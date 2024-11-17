package com.moin.demomoin.adapter.out.persistence.repository;

import com.moin.demomoin.adapter.out.persistence.entity.MoinTransferRequest;
import java.math.BigDecimal;
import java.time.Instant;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface MoinTransferRequestRepository extends ReactiveCrudRepository<MoinTransferRequest, Long> {

  @Query("""
            SELECT (SUM(request.USD_AMOUNT) + (
                                             SELECT USD_AMOUNT from moin_transfer_quote quote
                                             WHERE quote.id = :newQuoteId
                                             )) as total
            FROM moin_transfer_request request
            WHERE request.user_id = :userId
            AND request.requested_date BETWEEN :start AND :end
              group by request.USD_AMOUNT
      """)
  Flux<BigDecimal> sumTotalUsdAmount(String userId, Instant start, Instant end, Long newQuoteId);
}
