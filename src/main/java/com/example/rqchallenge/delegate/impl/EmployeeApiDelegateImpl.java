package com.example.rqchallenge.delegate.impl;

import com.example.rqchallenge.controller.EmployeesApiDelegate;
import com.example.rqchallenge.model.Employee;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class EmployeeApiDelegateImpl implements EmployeesApiDelegate {

  @Override
  public ResponseEntity<Employee> createEmployee(Employee employee) {
    return EmployeesApiDelegate.super.createEmployee(employee);
  }

  @Override
  public ResponseEntity<Void> deleteEmployeeById(Long id) {
    return EmployeesApiDelegate.super.deleteEmployeeById(id);
  }

  @Override
  public ResponseEntity<List<Employee>> getAllEmployees() {
    return EmployeesApiDelegate.super.getAllEmployees();
  }

  @Override
  public ResponseEntity<Employee> getEmployeeById(Long id) {
    return EmployeesApiDelegate.super.getEmployeeById(id);
  }

  @Override
  public ResponseEntity<List<Employee>> getEmployeesByNameSearch(String searchString) {
    return EmployeesApiDelegate.super.getEmployeesByNameSearch(searchString);
  }

  @Override
  public ResponseEntity<Long> getHighestSalaryOfEmployees() {
    return EmployeesApiDelegate.super.getHighestSalaryOfEmployees();
  }

  @Override
  public ResponseEntity<List<Employee>> getTopTenHighestEarningEmployees() {
    return EmployeesApiDelegate.super.getTopTenHighestEarningEmployees();
  }
}
