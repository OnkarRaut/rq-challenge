package com.example.rqchallenge.exception;

import org.springframework.http.HttpStatus;

public enum ErrorCodes implements Error {
  PARAM_IS_MISSING("PARAM_IS_MISSING");

  private final String code;
  private HttpStatus status = HttpStatus.BAD_REQUEST;

  ErrorCodes(String code) {
    this.code = code;
  }

  @Override
  public String getCode() {
    return code;
  }

  @Override
  public HttpStatus getHttpStatus() {
    return status;
  }
}
