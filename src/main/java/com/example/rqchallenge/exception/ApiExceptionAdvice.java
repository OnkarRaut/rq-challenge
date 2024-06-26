package com.example.rqchallenge.exception;

import com.example.rqchallenge.model.ApiErrorResponse;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@Component
@RestControllerAdvice(basePackages = "com.example.rqchallenge.controller")
public class ApiExceptionAdvice {

  @ExceptionHandler(value = RqException.class)
  public ResponseEntity<ApiErrorResponse> handleRqException(
      HttpServletRequest httpRequest, RqException exception) {
    log.error(
        "Exception occurred while processing request: {}-{}.",
        httpRequest.getMethod(),
        httpRequest.getRequestURI(),
        exception);

    ApiErrorResponse apiErrorResponse =
        getApiErrorResponse(exception.getError().getHttpStatus(), exception.getMessage());
    return ResponseEntity.status(exception.getError().getHttpStatus()).body(apiErrorResponse);
  }

  @ExceptionHandler(value = Exception.class)
  public ResponseEntity<ApiErrorResponse> handleException(
      HttpServletRequest httpRequest, Exception exception) {
    log.error(
        "Exception occurred while processing request: {}-{}.",
        httpRequest.getMethod(),
        httpRequest.getRequestURI(),
        exception);

    ApiErrorResponse apiErrorResponse =
        getApiErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, exception.getMessage());
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiErrorResponse);
  }

  private ApiErrorResponse getApiErrorResponse(HttpStatus httpStatus, String message) {
    ApiErrorResponse apiErrorResponse = new ApiErrorResponse();
    apiErrorResponse.setErrorCode(httpStatus.name());
    apiErrorResponse.setMessage(message);
    return apiErrorResponse;
  }
}
