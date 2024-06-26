package com.example.rqchallenge.delegate.impl;

import com.example.rqchallenge.controller.EmployeesApiDelegate;
import com.example.rqchallenge.model.Employee;
import com.example.rqchallenge.service.impl.EmployeeServiceImpl;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class EmployeeApiDelegateImpl implements EmployeesApiDelegate {

  private final EmployeeServiceImpl employeeService;

  @Override
  public ResponseEntity<Employee> createEmployee(Employee employee) {
    log.debug("Creating user.");
    Employee createdEmployee = employeeService.createEmployee(employee);
    log.debug("User created.");
    return ResponseEntity.ok(createdEmployee);
  }

  @Override
  public ResponseEntity<List<Employee>> getAllEmployees() {
    log.debug("Getting all employees.");
    List<Employee> employees = employeeService.getAllEmployees();
    return ResponseEntity.ok(employees);
  }

  @Override
  public ResponseEntity<Employee> getEmployeeById(Long id) {
    log.debug("Getting employee by id: {}.", id);
    Employee employee = employeeService.getEmployeeById(id);
    return ResponseEntity.ok(employee);
  }

  @Override
  public ResponseEntity<Void> deleteEmployeeById(Long id) {
    employeeService.deleteEmployeeById(id);
    return ResponseEntity.ok().build();
  }

  @Override
  public ResponseEntity<List<Employee>> getTopTenHighestEarningEmployees() {
    List<Employee> employees = employeeService.getTopTenHighestEarningEmployees();
    return ResponseEntity.ok(employees);
  }

  @Override
  public ResponseEntity<Long> getHighestSalaryOfEmployees() {
    Long highestSalary = employeeService.getHighestSalaryOfEmployees();
    return ResponseEntity.ok(highestSalary);
  }

  @Override
  public ResponseEntity<List<Employee>> getEmployeesByNameSearch(String searchString) {
    List<Employee> employees = employeeService.getEmployeesByNameSearch(searchString.toUpperCase());
    return ResponseEntity.ok(employees);
  }
}
