package com.example.rqchallenge.repository;

import com.example.rqchallenge.model.DeleteEmployeeResponse;
import com.example.rqchallenge.model.EmployeeDetails;
import com.example.rqchallenge.model.EmployeeDetailsResponse;
import com.example.rqchallenge.model.EmployeesDetailsResponse;

public interface EmployeeRepository {

  EmployeeDetails createEmployee(EmployeeDetails employeeDetails);

  DeleteEmployeeResponse deleteEmployeeById(Long id);

  EmployeesDetailsResponse getAllEmployees();

  EmployeeDetailsResponse getEmployeeById(Long id);
}
