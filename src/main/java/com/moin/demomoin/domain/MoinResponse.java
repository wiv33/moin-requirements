package com.moin.demomoin.domain;

import java.util.LinkedHashMap;
import java.util.Map;

public record MoinResponse(
    int resultCode,
    String resultMsg
) {

  public MoinResponse() {
    this(MoinStatusCode.OK.getStatus(), MoinStatusCode.OK.getCode());
  }

  public MoinResponse(MoinStatusCode code) {
    this(code.getStatus(), code.getCode());
  }

  public MoinResponse(int resultCode, String resultMsg) {
    this.resultCode = resultCode;
    this.resultMsg = resultMsg;
  }

  public static Map<String, Object> mergeBody(MoinResponse response, Map<String, Object> map) {
    var result = new LinkedHashMap<String, Object>();
    result.put("resultCode", response.resultCode);
    result.put("resultMsg", response.resultMsg);
    result.putAll(map);
    return result;
  }
  public static Map<String, Object> mergeBody(Map<String, Object> map) {
    var defaultResponse = new MoinResponse();
    return mergeBody(defaultResponse, map);
  }

  public static MoinResponse ok() {
    return new MoinResponse();
  }

  public static MoinResponse unauthorized() {
    return new MoinResponse(MoinStatusCode.UNAUTHORIZED);
  }

  public static MoinResponse badRequest(MoinStatusCode code) {
    return new MoinResponse(code);
  }
}
