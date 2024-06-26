package com.example.rqchallenge.service;

import com.example.rqchallenge.model.Employee;
import java.util.List;

public interface EmployeeService {
  List<Employee> getAllEmployees();

  Employee createEmployee(Employee employee);

  Employee getEmployeeById(Long id);

  List<Employee> getTopTenHighestEarningEmployees();

  void deleteEmployeeById(Long id);

  Long getHighestSalaryOfEmployees();

  List<Employee> getEmployeesByNameSearch(String searchString);
}
