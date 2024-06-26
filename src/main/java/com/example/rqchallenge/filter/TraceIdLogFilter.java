package com.example.rqchallenge.filter;

import java.io.IOException;
import java.util.UUID;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@Order(1)
public class TraceIdLogFilter implements Filter {

  private static final String TRACE_ID = "traceId";

  @Override
  public void doFilter(
      ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
      throws IOException, ServletException {
    try {

      HttpServletRequest request = (HttpServletRequest) servletRequest;
      HttpServletResponse response = (HttpServletResponse) servletResponse;

      String traceId = UUID.randomUUID().toString();
      MDC.put(TRACE_ID, traceId);
      response.addHeader(TRACE_ID, traceId);

      long startTime = System.currentTimeMillis();

      log.debug(
          "Request received with server name: {} and host: {}",
          request.getServerName(),
          request.getRemoteHost());

      log.info("Request received: {} - {}", request.getMethod(), request.getRequestURI());

      filterChain.doFilter(request, response);

      log.info(
          "Request completed: {} - {}. Execution time: {}ms. Response {}.",
          request.getMethod(),
          request.getRequestURI(),
          System.currentTimeMillis() - startTime,
          response.getStatus());
    } finally {
      MDC.remove(TRACE_ID);
    }
  }
}
