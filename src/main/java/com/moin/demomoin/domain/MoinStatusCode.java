package com.moin.demomoin.domain.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum MoinStatusCode {
  OK(200, "OK", "성공"),
  FAIL(400, "FAIL", "실패"),
  ERROR(500, "ERROR", "오류"),


  INVALID_ARGUMENT(400, "INVALID_ARGUMENT", "잘못된 요청입니다."),
  INVALID_ARGUMENT_EMAIL_FORMAT(400, "INVALID_ARGUMENT_EMAIL_FORMAT", "올바른 이메일 형식을 입력해야 합니다."),
  INVALID_USER_ID(400, "INVALID_USER_ID", "아이디는 이메일 형식이어야 합니다."),
  USER_ALREADY_EXISTS(400, "USER_ALREADY_EXISTS", "이미 존재하는 사용자입니다."),
  USER_NOT_FOUND(400, "USER_NOT_FOUND", "사용자를 찾을 수 없습니다."),

  NEGATIVE_NUMBER(500, "NEGATIVE_NUMBER", "보내는 금액은 0보다 커야합니다."),

  UNAUTHORIZED(401, "UNAUTHORIZED", "인증되지 않은 사용자입니다."),
  INVALID_TOKEN_EXPIRED(403, "INVALID_TOKEN_EXPIRED", "만료된 토큰입니다."),
  FORBIDDEN(403, "FORBIDDEN", "권한이 없습니다."),
  LIMIT_EXCESS(500, "LIMIT_EXCESS", "1일 한도 초과입니다."),
  QUOTE_EXPIRED(500, "QUOTE_EXPIRED", "견적이 만료되었습니다."),
  UNKNOWN_ERROR(500, "UNKNOWN_ERROR", "서버가 알 수 없는 오류가 발생했습니다."),
  ;

  private final int status;
  private final String code;
  private final String message;
}
