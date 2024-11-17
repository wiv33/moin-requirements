package com.moin.demomoin.adapter.in.web.config;

import com.moin.demomoin.domain.MoinResponse;
import com.moin.demomoin.domain.exception.MoinInvalidArgumentException;
import com.moin.demomoin.domain.exception.MoinNotFoundException;
import com.moin.demomoin.domain.MoinStatusCode;
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
  @ExceptionHandler(MoinNotFoundException.class)
  public Mono<MoinResponse> handleResourceNotFoundException(MoinNotFoundException ex) {
    return Mono.create(
        sink -> sink.success(new MoinResponse(HttpStatus.NOT_FOUND.value(), ex.getMessage()))
    );
  }

  @ExceptionHandler(MoinInvalidArgumentException.class)
  public Mono<MoinResponse> handleInvalidArgumentException(MoinInvalidArgumentException ex) {
    return Mono.create(
        sink -> sink.success(new MoinResponse(MoinStatusCode.INVALID_ARGUMENT.getStatus(), ex.getMessage()))
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
