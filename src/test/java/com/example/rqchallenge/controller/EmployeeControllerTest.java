package com.example.rqchallenge.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

import com.example.rqchallenge.client.EmployeesApi;
import com.example.rqchallenge.model.*;
import com.example.rqchallenge.service.EmployeeServiceTest;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpServerErrorException;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class EmployeeControllerTest {

  @LocalServerPort private int port;

  @Autowired private TestRestTemplate testRestTemplate;

  @MockBean private EmployeesApi employeesApi;

  @Test
  void testGetAllEmployees() {
    EmployeesDetailsResponse employeesDetailsResponse = new EmployeesDetailsResponse();
    employeesDetailsResponse.setData(
        IntStream.range(0, 20)
            .mapToObj(i -> EmployeeServiceTest.getRandomEmployeeDetail())
            .collect(Collectors.toList()));

    when(employeesApi.getAllEmployees()).thenReturn(employeesDetailsResponse);

    ResponseEntity<List> response =
        testRestTemplate.getForEntity("http://localhost:" + port + "/api/employees", List.class);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(20, response.getBody().size());
  }

  @Test
  void testGetAllEmployeesServerErrorWithRetry() {
    when(employeesApi.getAllEmployees())
        .thenThrow(new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR));
    ApiErrorResponse response =
        testRestTemplate.getForObject(
            "http://localhost:" + port + "/api/employees", ApiErrorResponse.class);

    assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.name(), response.getErrorCode());
    verify(employeesApi, times(3)).getAllEmployees();
  }

  @Test
  void testGetEmployeeById() {
    EmployeeDetailsResponse employeeDetailsResponse = new EmployeeDetailsResponse();
    EmployeeDetails returnedDetails = EmployeeServiceTest.getRandomEmployeeDetail();
    employeeDetailsResponse.setData(returnedDetails);

    when(employeesApi.getEmployeeById(anyLong())).thenReturn(employeeDetailsResponse);
    ResponseEntity<Employee> response =
        testRestTemplate.getForEntity(
            "http://localhost:" + port + "/api/employees/10", Employee.class);
    assertEquals(HttpStatus.OK, response.getStatusCode());

    assertEquals(response.getBody().getEmployeeAge(), returnedDetails.getEmployeeAge());
    assertEquals(response.getBody().getEmployeeName(), returnedDetails.getEmployeeName());
    assertEquals(response.getBody().getEmployeeSalary(), returnedDetails.getEmployeeSalary());
  }

  @Test
  void testGetEmployeeIdServerErrorWithRetry() {
    when(employeesApi.getEmployeeById(anyLong()))
        .thenThrow(new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR));
    ApiErrorResponse response =
        testRestTemplate.getForObject(
            "http://localhost:" + port + "/api/employees/10", ApiErrorResponse.class);
    assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.name(), response.getErrorCode());
    verify(employeesApi, times(3)).getEmployeeById(anyLong());
  }

  @Test
  void testGetTopTenHighestEarningEmployees() {
    EmployeesDetailsResponse employeesDetailsResponse = new EmployeesDetailsResponse();
    employeesDetailsResponse.setData(
        IntStream.range(0, 20)
            .mapToObj(i -> EmployeeServiceTest.getRandomEmployeeDetail())
            .collect(Collectors.toList()));

    when(employeesApi.getAllEmployees()).thenReturn(employeesDetailsResponse);

    ResponseEntity<List> response =
        testRestTemplate.getForEntity(
            "http://localhost:" + port + "/api/employees/top_ten_highest_earning_employees",
            List.class);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(10, response.getBody().size());
  }

  @Test
  void testGetTopTenHighestEarningEmployeesServerErrorWithRetry() {
    when(employeesApi.getAllEmployees())
        .thenThrow(new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR));
    ApiErrorResponse response =
        testRestTemplate.getForObject(
            "http://localhost:" + port + "/api/employees/top_ten_highest_earning_employees",
            ApiErrorResponse.class);
    assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.name(), response.getErrorCode());
    verify(employeesApi, times(3)).getAllEmployees();
  }

  @Test
  void testCreateEmployee() {
    EmployeeDetailsResponse employeeDetailsResponse = new EmployeeDetailsResponse();
    EmployeeDetails returnedDetails = EmployeeServiceTest.getRandomEmployeeDetail();
    employeeDetailsResponse.setData(returnedDetails);

    when(employeesApi.createEmployee(any(EmployeeDetails.class)))
        .thenReturn(employeeDetailsResponse);

    Employee employee = new Employee();
    employee.setEmployeeName(UUID.randomUUID().toString());
    employee.setEmployeeAge(EmployeeServiceTest.randomInRange(25, 40));
    employee.setEmployeeSalary(Long.valueOf(EmployeeServiceTest.randomInRange(25000, 30000)));

    ResponseEntity<Employee> response =
        testRestTemplate.postForEntity(
            "http://localhost:" + port + "/api/employees", employee, Employee.class);

    assertEquals(HttpStatus.OK, response.getStatusCode());
  }

  @Test
  void testCreateEmployeeServerErrorWithRetry() {
    when(employeesApi.createEmployee(any(EmployeeDetails.class)))
        .thenThrow(new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR));

    Employee employee = new Employee();
    employee.setEmployeeName(UUID.randomUUID().toString());
    employee.setEmployeeAge(EmployeeServiceTest.randomInRange(25, 40));
    employee.setEmployeeSalary(Long.valueOf(EmployeeServiceTest.randomInRange(25000, 30000)));

    ApiErrorResponse response =
        testRestTemplate.postForObject(
            "http://localhost:" + port + "/api/employees", employee, ApiErrorResponse.class);
    assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.name(), response.getErrorCode());
    verify(employeesApi, times(3)).createEmployee(any(EmployeeDetails.class));
  }

  @Test
  void testCreateEmployeeBadRequest() {
    Employee employee = new Employee();
    ApiErrorResponse response =
        testRestTemplate.postForObject(
            "http://localhost:" + port + "/api/employees", employee, ApiErrorResponse.class);

    assertEquals(HttpStatus.BAD_REQUEST.name(), response.getErrorCode());
    assertEquals("Param 'employee_name' is missing.", response.getMessage());

    employee.setEmployeeName(UUID.randomUUID().toString());
    ApiErrorResponse response1 =
        testRestTemplate.postForObject(
            "http://localhost:" + port + "/api/employees", employee, ApiErrorResponse.class);

    assertEquals(HttpStatus.BAD_REQUEST.name(), response1.getErrorCode());
    assertEquals("Param 'employee_age' is missing.", response1.getMessage());

    employee.setEmployeeAge(25);
    ApiErrorResponse response2 =
        testRestTemplate.postForObject(
            "http://localhost:" + port + "/api/employees", employee, ApiErrorResponse.class);

    assertEquals(HttpStatus.BAD_REQUEST.name(), response2.getErrorCode());
    assertEquals("Param 'employee_salary' is missing.", response2.getMessage());
  }

  @Test
  void deleteEmployeeById() {
    when(employeesApi.deleteEmployeeById(anyLong())).thenReturn(new DeleteEmployeeResponse());
    testRestTemplate.delete("http://localhost:" + port + "/api/employees/1");
  }

  @Test
  void deleteEmployeeByIdServerError() {
    when(employeesApi.deleteEmployeeById(anyLong()))
        .thenThrow(new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR));
    ResponseEntity<ApiErrorResponse> apiErrorResponse =
        testRestTemplate.exchange(
            "http://localhost:" + port + "/api/employees/1",
            HttpMethod.DELETE,
            null,
            ApiErrorResponse.class);

    assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, apiErrorResponse.getStatusCode());
  }

  @Test
  void testGetAllEmployeesByNameSearch() {
    EmployeesDetailsResponse employeesDetailsResponse = new EmployeesDetailsResponse();
    employeesDetailsResponse.setData(
        Stream.concat(
                IntStream.range(0, 20)
                    .mapToObj(i -> EmployeeServiceTest.getRandomEmployeeDetail("one")),
                IntStream.range(0, 20).mapToObj(i -> EmployeeServiceTest.getRandomEmployeeDetail()))
            .collect(Collectors.toList()));

    when(employeesApi.getAllEmployees()).thenReturn(employeesDetailsResponse);

    ResponseEntity<List> response =
        testRestTemplate.getForEntity(
            "http://localhost:" + port + "/api/employees/search/one", List.class);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(20, response.getBody().size());
  }

  @Test
  void testGetHighestSalaryOfEmployees() {
    EmployeesDetailsResponse employeesDetailsResponse = new EmployeesDetailsResponse();
    employeesDetailsResponse.setData(
        Stream.concat(
                IntStream.range(0, 20)
                    .mapToObj(i -> EmployeeServiceTest.getRandomEmployeeDetail("one")),
                IntStream.range(0, 20).mapToObj(i -> EmployeeServiceTest.getRandomEmployeeDetail()))
            .collect(Collectors.toList()));

    when(employeesApi.getAllEmployees()).thenReturn(employeesDetailsResponse);

    ResponseEntity<Long> response =
        testRestTemplate.getForEntity(
            "http://localhost:" + port + "/api/employees/highest_salary", Long.class);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());
  }
}
