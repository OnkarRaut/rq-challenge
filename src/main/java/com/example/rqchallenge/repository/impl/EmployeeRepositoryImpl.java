package com.example.rqchallenge.repository.impl;

import com.example.rqchallenge.annotation.RetryOnServerErrors;
import com.example.rqchallenge.client.EmployeesApi;
import com.example.rqchallenge.model.DeleteEmployeeResponse;
import com.example.rqchallenge.model.EmployeeDetails;
import com.example.rqchallenge.model.EmployeeDetailsResponse;
import com.example.rqchallenge.model.EmployeesDetailsResponse;
import com.example.rqchallenge.repository.EmployeeRepository;
import io.micrometer.core.annotation.Timed;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class EmployeeRepositoryImpl implements EmployeeRepository {

  private final EmployeesApi employeesApi;

  @Override
  @Timed(
      value = "employees_api.create",
      histogram = true,
      percentiles = {0.5, 0.75, 0.90})
  @RetryOnServerErrors
  public EmployeeDetails createEmployee(EmployeeDetails employeeDetails) {
    log.debug("Creating employee.");
    return employeesApi.createEmployee(employeeDetails);
  }

  @Override
  @Timed(
      value = "employees_api.delete",
      histogram = true,
      percentiles = {0.5, 0.75, 0.90})
  @RetryOnServerErrors
  public DeleteEmployeeResponse deleteEmployeeById(Long id) {
    log.debug("Deleting employee.");
    return employeesApi.deleteEmployeeById(id);
  }

  @Override
  @Timed(
      value = "employees_api.all",
      histogram = true,
      percentiles = {0.5, 0.75, 0.90})
  @RetryOnServerErrors
  public EmployeesDetailsResponse getAllEmployees() {
    log.debug("Getting employee details.");
    return employeesApi.getAllEmployees();
  }

  @Override
  @Timed(
      value = "employees_api.get_by_id",
      histogram = true,
      percentiles = {0.5, 0.75, 0.90})
  @RetryOnServerErrors
  public EmployeeDetailsResponse getEmployeeById(Long id) {
    log.debug("Getting employee by id: {}.", id);
    return employeesApi.getEmployeeById(id);
  }
}
