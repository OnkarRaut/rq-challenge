package com.example.rqchallenge.config;

import com.example.rqchallenge.ApiClient;
import com.example.rqchallenge.client.EmployeesApi;
import java.time.Duration;
import java.util.Set;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.web.client.RestTemplate;

@EnableRetry
@Configuration
public class ClientConfig {

  @Value("${employees_service.base_path:https://dummy.restapiexample.com}")
  private String employeesServiceBasePath;

  @Value("#{new Integer('${employees_service.read_timeout:60000}')}")
  private Integer readTimeout;

  @Value("#{new Integer('${employees_service.connection_timeout:60000}')}")
  private Integer connectionTimeout;

  private final Set<Integer> RETRY_ELIGIBLE_STATUS =
      Set.of(
          HttpStatus.BAD_GATEWAY.value(),
          HttpStatus.SERVICE_UNAVAILABLE.value(),
          HttpStatus.INTERNAL_SERVER_ERROR.value(),
          HttpStatus.GATEWAY_TIMEOUT.value());

  @Bean
  public EmployeesApi employeesApi() {
    EmployeesApi employeesApi = new EmployeesApi();
    ApiClient apiClient = new ApiClient(restTemplate());
    apiClient.setBasePath(employeesServiceBasePath);
    employeesApi.setApiClient(new ApiClient());
    employeesApi.setApiClient(apiClient);
    return employeesApi;
  }

  @Bean
  public RestTemplate restTemplate() {
    return new RestTemplateBuilder()
        .setConnectTimeout(Duration.ofMillis(connectionTimeout))
        .setReadTimeout(Duration.ofMillis(readTimeout))
        .build();
  }
}
