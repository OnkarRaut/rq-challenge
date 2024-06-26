package com.example.rqchallenge.exception;

import org.springframework.http.HttpStatus;

public interface Error {

  String getCode();

  HttpStatus getHttpStatus();
}
