package com.example.rqchallenge.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import com.example.rqchallenge.client.EmployeesApi;
import com.example.rqchallenge.exception.RqException;
import com.example.rqchallenge.model.Employee;
import com.example.rqchallenge.model.EmployeeDetails;
import com.example.rqchallenge.model.EmployeeDetailsResponse;
import com.example.rqchallenge.model.EmployeesDetailsResponse;
import com.example.rqchallenge.service.impl.EmployeeServiceImpl;
import com.google.common.collect.Comparators;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class EmployeeServiceTest {

  @MockBean private EmployeesApi employeesApi;

  @Autowired private EmployeeServiceImpl employeeService;

  @BeforeEach
  void init() {
    employeeService.setMaxEmployeeFetchLimit(10);
  }

  @Test
  void testGetAllEmployees() {
    EmployeesDetailsResponse employeesDetailsResponse = new EmployeesDetailsResponse();
    employeesDetailsResponse.setData(
        IntStream.range(0, 20)
            .mapToObj(i -> EmployeeServiceTest.getRandomEmployeeDetail())
            .collect(Collectors.toList()));

    when(employeesApi.getAllEmployees()).thenReturn(employeesDetailsResponse);
    List<Employee> employees = employeeService.getAllEmployees();
    assertEquals(20, employees.size());
  }

  @Test
  void testCreateEmployee() {
    EmployeeDetails returnedDetails = getRandomEmployeeDetail();
    when(employeesApi.createEmployee(any(EmployeeDetails.class))).thenReturn(returnedDetails);

    Employee employee = new Employee();
    employee.setEmployeeName(UUID.randomUUID().toString());
    employee.setEmployeeAge(randomInRange(25, 40));
    employee.setEmployeeSalary(Long.valueOf(randomInRange(25000, 30000)));

    Employee createdEmployee = employeeService.createEmployee(employee);
    assertEquals(createdEmployee.getEmployeeAge(), returnedDetails.getEmployeeAge());
    assertEquals(createdEmployee.getEmployeeName(), returnedDetails.getEmployeeName());
    assertEquals(createdEmployee.getEmployeeSalary(), returnedDetails.getEmployeeSalary());
  }

  @Test
  void testCreateEmployeeInvalidRequest() {
    assertThrows(RqException.class, () -> employeeService.createEmployee(new Employee()));
  }

  @Test
  void testGetEmployeeById() {
    EmployeeDetailsResponse employeeDetailsResponse = new EmployeeDetailsResponse();
    employeeDetailsResponse.setData(getRandomEmployeeDetail());

    when(employeesApi.getEmployeeById(anyLong())).thenReturn(employeeDetailsResponse);
    Employee employee = employeeService.getEmployeeById(10L);
    assertNotNull(employee.getEmployeeAge());
    assertNotNull(employee.getEmployeeName());
    assertNotNull(employee.getEmployeeSalary());
  }

  @Test
  void testGetTopTenHighestEarningEmployees() {
    EmployeesDetailsResponse employeesDetailsResponse = new EmployeesDetailsResponse();
    employeesDetailsResponse.setData(
        IntStream.range(0, 20)
            .mapToObj(i -> EmployeeServiceTest.getRandomEmployeeDetail())
            .collect(Collectors.toList()));

    when(employeesApi.getAllEmployees()).thenReturn(employeesDetailsResponse);

    List<Employee> employees = employeeService.getTopTenHighestEarningEmployees();

    assertEquals(10, employees.size());
    assertTrue(
        Comparators.isInOrder(
            employees.stream().map(Employee::getEmployeeSalary).collect(Collectors.toList()),
            Comparator.reverseOrder()));
  }

  public static EmployeeDetails getRandomEmployeeDetail() {
    return getRandomEmployeeDetail(null);
  }

  public static EmployeeDetails getRandomEmployeeDetail(String prefix) {
    EmployeeDetails employeeDetails = new EmployeeDetails();
    if (StringUtils.isNotBlank(prefix)) {
      employeeDetails.setEmployeeName(prefix + UUID.randomUUID());
    } else {
      employeeDetails.setEmployeeName(UUID.randomUUID().toString());
    }
    employeeDetails.setEmployeeAge(randomInRange(25, 40));
    employeeDetails.setEmployeeSalary(Long.valueOf(randomInRange(25000, 40000)));
    return employeeDetails;
  }

  public static int randomInRange(int start, int end) {
    return new Random().ints(start, end).findFirst().getAsInt();
  }
}
