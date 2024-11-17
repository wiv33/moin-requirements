package com.moin.demomoin.adapter.in.web.config.auth;

import com.moin.demomoin.domain.MoinResponse;
import com.moin.demomoin.domain.exception.MoinStatusCode;
import com.moin.demomoin.domain.exception.MoinUserAlreadyExistsException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.handler.ResponseStatusExceptionHandler;
import reactor.core.publisher.Mono;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseStatusExceptionHandler {

  @ExceptionHandler(MoinUserAlreadyExistsException.class)
  public Mono<MoinResponse> handleResourceNotFoundException(MoinUserAlreadyExistsException ex) {
    return Mono.create(
        sink -> sink.success(new MoinResponse(HttpStatus.BAD_REQUEST.value(), ex.getMessage()))
    );
  }

  @ExceptionHandler({RuntimeException.class, Exception.class, Throwable.class})
  public Mono<MoinResponse> handleCommonException(Throwable ex) {
    return Mono.create(sink -> sink.success(
        new MoinResponse(MoinStatusCode.UNKNOWN_ERROR.getStatus(), ex.getMessage())));
  }

//  @ExceptionHandler(BadRequestException.class)
//  public Mono<ResponseEntity<String>> handleBadRequestException(BadRequestException ex,
//      ServerWebExchange exchange) {
//    return Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage()));
//  }
//
//  @ExceptionHandler(Exception.class)
//  public Mono<ResponseEntity<String>> handleGenericException(Exception ex,
//      ServerWebExchange exchange) {
//    return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//        .body("An unexpected error occurred: " + ex.getMessage()));
//  }
}
