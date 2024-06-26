package com.example.rqchallenge.service.impl;

import static java.util.Objects.isNull;

import com.example.rqchallenge.exception.ErrorCodes;
import com.example.rqchallenge.exception.RqException;
import com.example.rqchallenge.model.Employee;
import com.example.rqchallenge.model.EmployeeDetails;
import com.example.rqchallenge.repository.EmployeeRepository;
import com.example.rqchallenge.service.EmployeeService;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class EmployeeServiceImpl implements EmployeeService {

  @Autowired private EmployeeRepository employeeRepository;

  @Setter
  @Value("#{new Integer('${employee_fetch_limit.max:10}')}")
  private Integer maxEmployeeFetchLimit;

  @Override
  public List<Employee> getAllEmployees() {
    List<Employee> employees =
        employeeRepository.getAllEmployees().getData().stream()
            .map(this::toEmployee)
            .collect(Collectors.toList());

    log.debug("Found {} employee details.", employees.size());

    return employees;
  }

  @Override
  public Employee createEmployee(Employee employee) {
    validateCreateEmployeeRequest(employee);

    EmployeeDetails employeeDetails =
        employeeRepository.createEmployee(toEmployeeDetails(employee));
    return toEmployee(employeeDetails);
  }

  @Override
  public Employee getEmployeeById(Long id) {
    EmployeeDetails employeeDetails = employeeRepository.getEmployeeById(id).getData();
    return toEmployee(employeeDetails);
  }

  @Override
  public List<Employee> getTopTenHighestEarningEmployees() {
    return getAllEmployees().stream()
        .filter(employeeDetails -> Objects.nonNull(employeeDetails.getEmployeeSalary()))
        .sorted(Comparator.comparing(Employee::getEmployeeSalary).reversed())
        .limit(maxEmployeeFetchLimit)
        .collect(Collectors.toList());
  }

  @Override
  public void deleteEmployeeById(Long id) {
    employeeRepository.deleteEmployeeById(id);
  }

  @Override
  public Long getHighestSalaryOfEmployees() {
    return getTopTenHighestEarningEmployees().get(0).getEmployeeSalary();
  }

  @Override
  public List<Employee> getEmployeesByNameSearch(String searchString) {
    return getAllEmployees().stream()
        .filter(employee -> employee.getEmployeeName().toUpperCase().contains(searchString))
        .collect(Collectors.toList());
  }

  private Employee toEmployee(EmployeeDetails employeeDetails) {
    Employee employee = new Employee();
    employee.setEmployeeName(employeeDetails.getEmployeeName());
    employee.setEmployeeAge(employeeDetails.getEmployeeAge());
    employee.setEmployeeSalary(employeeDetails.getEmployeeSalary());
    return employee;
  }

  private EmployeeDetails toEmployeeDetails(Employee employee) {
    EmployeeDetails employeeDetails = new EmployeeDetails();
    employeeDetails.setEmployeeName(employee.getEmployeeName());
    employeeDetails.setEmployeeAge(employee.getEmployeeAge());
    employeeDetails.setEmployeeSalary(employee.getEmployeeSalary());
    return employeeDetails;
  }

  private void validateCreateEmployeeRequest(Employee employee) {
    log.debug("Validation create employee request.");
    if (StringUtils.isBlank(employee.getEmployeeName())) {
      log.error("Missing required param name.");
      throw new RqException(ErrorCodes.PARAM_IS_MISSING, "employee_name");
    }

    if (isNull(employee.getEmployeeAge())) {
      log.error("Missing required param age.");
      throw new RqException(ErrorCodes.PARAM_IS_MISSING, "employee_age");
    }

    if (isNull(employee.getEmployeeSalary())) {
      log.error("Missing required param salary.");
      throw new RqException(ErrorCodes.PARAM_IS_MISSING, "employee_salary");
    }
  }
}
