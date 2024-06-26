package com.example.rqchallenge.exception;

import com.example.rqchallenge.util.LocaleUtils;
import lombok.Getter;

@Getter
public class RqException extends RuntimeException {

  private Error error;
  private Object[] args;

  public RqException(Error error, Object... args) {
    super();
    this.error = error;
    this.args = args;
  }

  @Override
  public String getMessage() {
    return LocaleUtils.getMessage(getError().getCode(), getArgs());
  }
}
