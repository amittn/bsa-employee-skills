package uk.nhs.nhsbsa.employeeskills.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import uk.nhs.nhsbsa.employeeskills.entity.Employee;
import uk.nhs.nhsbsa.employeeskills.entity.Skills;
import uk.nhs.nhsbsa.employeeskills.repository.EmployeeRepository;
import uk.nhs.nhsbsa.employeeskills.repository.SkillsRepository;
import uk.nhs.nhsbsa.employeeskills.request.EmployeeRegistrationRequest;
import uk.nhs.nhsbsa.employeeskills.response.EmployeeResponse;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest
class EmployeeRegistrationServiceTest {

  @MockBean private EmployeeRepository employeeRepository;

  @MockBean private SkillsRepository skillsRepository;

  @Autowired private EmployeeRegistrationService employeeRegistrationService;

  @Test
  void test_registerEmployee_returnsEmployeeResponse_whenRequestIsValid() {
    EmployeeRegistrationRequest employeeRegistrationRequest =
        EmployeeRegistrationRequest.builder()
            .givenName("abc")
            .familyName("xyz")
            .dateOfBirth("2016-04-01")
            .build();
    Employee persistedEmployee =
        Employee.builder()
            .empId(1L)
            .givenName("abc")
            .familyName("xyz")
            .dateOfBirth("2016-04-01")
            .build();
    EmployeeResponse expectedResponse =
        EmployeeResponse.builder()
            .empId("1")
            .message("Successfully registered")
            .status(HttpStatus.CREATED.value())
            .responseStatus(HttpStatus.CREATED)
            .givenName("abc")
            .familyName("xyz")
            .dateOfBirth("2016-04-01")
            .build();

    when(employeeRepository.save(any(Employee.class))).thenReturn(persistedEmployee);

    EmployeeResponse employeeResponse =
        employeeRegistrationService.registerEmployee(employeeRegistrationRequest);

    assertEquals(employeeResponse, expectedResponse);
    verify(employeeRepository, times(1)).save(any(Employee.class));
  }

  @Test
  void test_fetchEmployeeDetails_returnsEmployeeResponse_whenEmpIdIsValid() {
    Long longValue = 1L;
    Employee persistedEmployee =
        Employee.builder()
            .empId(longValue)
            .givenName("abc")
            .familyName("xyz")
            .dateOfBirth("2016-04-01")
            .build();
    Optional<Employee> empOptional = Optional.of(persistedEmployee);
    EmployeeResponse expectedResponse =
        EmployeeResponse.builder()
            .empId("1")
            .givenName("abc")
            .familyName("xyz")
            .dateOfBirth("2016-04-01")
            .responseStatus(HttpStatus.OK)
            .status(HttpStatus.OK.value())
            .message("Successfully fetched data")
            .build();

    when(employeeRepository.findById(any(Long.class))).thenReturn(empOptional);

    EmployeeResponse employeeResponse = employeeRegistrationService.fetchEmployeeDetails(longValue);

    assertEquals(employeeResponse, expectedResponse);
    verify(employeeRepository, times(1)).findById(longValue);
  }

  @Test
  void test_fetchEmployeeDetails_returnsEmpNotFount_whenEmpIdIsValid() {
    Long longValue = 1L;
    Optional<Employee> empOptional = Optional.empty();
    EmployeeResponse expectedResponse =
        EmployeeResponse.builder()
            .responseStatus(HttpStatus.OK)
            .status(HttpStatus.OK.value())
            .message("The requested empId does not exist")
            .build();

    when(employeeRepository.findById(any(Long.class))).thenReturn(empOptional);

    EmployeeResponse employeeResponse = employeeRegistrationService.fetchEmployeeDetails(longValue);

    assertEquals(employeeResponse, expectedResponse);
    verify(employeeRepository, times(1)).findById(longValue);
  }

  @Test
  void test_updateEmployeeDetails_returnsEmpNotFound_whenRequestAndEmpIdIsValid() {
    Long longValue = 1L;
    EmployeeRegistrationRequest employeeRegistrationRequest =
        EmployeeRegistrationRequest.builder()
            .givenName("abc1")
            .familyName("xyz1")
            .dateOfBirth("2016-04-01")
            .build();
    Optional<Employee> empOptional = Optional.empty();
    EmployeeResponse expectedResponse =
        EmployeeResponse.builder()
            .responseStatus(HttpStatus.OK)
            .status(HttpStatus.OK.value())
            .message("The requested empId does not exist")
            .build();

    when(employeeRepository.findById(any(Long.class))).thenReturn(empOptional);

    EmployeeResponse employeeResponse =
        employeeRegistrationService.updateEmployeeDetails(longValue, employeeRegistrationRequest);

    assertEquals(employeeResponse, expectedResponse);
    verify(employeeRepository, times(1)).findById(longValue);
  }

  @Test
  void test_updateEmployeeDetails_returnsEmployeeResponse_whenRequestAndEmpIdIsValid() {
    Long longValue = 1L;
    EmployeeRegistrationRequest employeeRegistrationRequest =
        EmployeeRegistrationRequest.builder()
            .givenName("abc1")
            .familyName("xyz1")
            .dateOfBirth("2016-04-01")
            .build();
    Employee persistedEmployee =
        Employee.builder()
            .empId(longValue)
            .givenName("abc")
            .familyName("xyz")
            .dateOfBirth("2016-04-01")
            .build();
    Optional<Employee> empOptional = Optional.of(persistedEmployee);
    EmployeeResponse expectedResponse =
        EmployeeResponse.builder()
            .responseStatus(HttpStatus.OK)
            .status(HttpStatus.OK.value())
            .message("Successfully updated employee data")
            .build();

    when(employeeRepository.findById(any(Long.class))).thenReturn(empOptional);

    EmployeeResponse employeeResponse =
        employeeRegistrationService.updateEmployeeDetails(longValue, employeeRegistrationRequest);

    assertEquals(employeeResponse, expectedResponse);
    verify(employeeRepository, times(1)).findById(longValue);
  }

  @Test
  void test_deleteEmployeeDetails_returnsEmployeeResponse_whenEmpIdIsValid() {
    Long longValue = 1L;
    Employee persistedEmployee =
        Employee.builder()
            .empId(longValue)
            .givenName("abc")
            .familyName("xyz")
            .dateOfBirth("2016-04-01")
            .build();
    Optional<Employee> empOptional = Optional.of(persistedEmployee);
    EmployeeResponse expectedResponse =
        EmployeeResponse.builder()
            .responseStatus(HttpStatus.OK)
            .status(HttpStatus.OK.value())
            .message("Successfully deleted employee")
            .build();

    when(employeeRepository.findById(any(Long.class))).thenReturn(empOptional);

    EmployeeResponse employeeResponse =
        employeeRegistrationService.deleteEmployeeDetails(longValue);

    assertEquals(employeeResponse, expectedResponse);
    verify(employeeRepository, times(1)).findById(longValue);
  }

  @Test
  void test_deleteEmployeeDetails_returnsEmpNotFound_whenEmpIdIsValid() {
    Long longValue = 1L;
    Optional<Employee> empOptional = Optional.empty();
    EmployeeResponse expectedResponse =
        EmployeeResponse.builder()
            .responseStatus(HttpStatus.OK)
            .status(HttpStatus.OK.value())
            .message("The requested empId does not exist")
            .build();

    when(employeeRepository.findById(any(Long.class))).thenReturn(empOptional);

    EmployeeResponse employeeResponse =
        employeeRegistrationService.deleteEmployeeDetails(longValue);

    assertEquals(employeeResponse, expectedResponse);
    verify(employeeRepository, times(1)).findById(longValue);
  }

  @Test
  void test_deleteSkillFromEmployee_returnsEmployeeResponse_whenEmpIdAndSkillIdIsValid() {
    Long longValue = 1L;
    Skills skills = Skills.builder().skill("Java").level("Working").build();
    Optional<Skills> skillOptional = Optional.of(skills);
    Set<Skills> skillsSet = new HashSet<Skills>();
    skillsSet.add(skills);
    Employee persistedEmployee =
        Employee.builder()
            .empId(longValue)
            .empSkillsSet(skillsSet)
            .givenName("abc")
            .familyName("xyz")
            .dateOfBirth("2016-04-01")
            .build();
    Optional<Employee> empOptional = Optional.of(persistedEmployee);

    Employee employeeWithSkillDeleted =
        Employee.builder()
            .empId(longValue)
            .givenName("abc")
            .familyName("xyz")
            .dateOfBirth("2016-04-01")
            .build();
    EmployeeResponse expectedResponse =
        EmployeeResponse.builder()
            .empId("1")
            .givenName("abc")
            .familyName("xyz")
            .dateOfBirth("2016-04-01")
            .responseStatus(HttpStatus.OK)
            .status(HttpStatus.OK.value())
            .message("Successfully deleted the skill")
            .build();

    when(employeeRepository.findById(any(Long.class))).thenReturn(empOptional);
    when(employeeRepository.save(any(Employee.class))).thenReturn(employeeWithSkillDeleted);
    when(skillsRepository.findById(any(Long.class))).thenReturn(skillOptional);

    EmployeeResponse employeeResponse =
        employeeRegistrationService.deleteSkillFromEmployee(longValue, longValue);

    assertEquals(employeeResponse, expectedResponse);
    verify(employeeRepository, times(1)).findById(longValue);
    verify(skillsRepository, times(1)).findById(longValue);
    verify(employeeRepository, times(1)).save(any(Employee.class));
  }

  @Test
  void test_deleteSkillFromEmployee_returnsEmpOrSkillNotFound_whenEmpIdAndSkillIdIsValid() {
    Long longValue = 1L;
    Optional<Skills> skillOptional = Optional.empty();
    Optional<Employee> empOptional = Optional.empty();

    EmployeeResponse expectedResponse =
        EmployeeResponse.builder()
            .responseStatus(HttpStatus.OK)
            .status(HttpStatus.OK.value())
            .message("The requested empId or skillId does not exist")
            .build();

    when(employeeRepository.findById(any(Long.class))).thenReturn(empOptional);
    when(skillsRepository.findById(any(Long.class))).thenReturn(skillOptional);

    EmployeeResponse employeeResponse =
        employeeRegistrationService.deleteSkillFromEmployee(longValue, longValue);

    assertEquals(employeeResponse, expectedResponse);
    verify(employeeRepository, times(1)).findById(longValue);
    verify(skillsRepository, times(1)).findById(longValue);
  }

  @Test
  void test_addSkillsToEmployee_returnsEmployeeResponse_whenEmpIdAndSkillIdIsValid() {
    Long longValue = 1L;
    Skills skills = Skills.builder().skill("Java").level("Working").build();
    Optional<Skills> skillOptional = Optional.of(skills);
    Set<Skills> skillsSetEmpty = new HashSet<Skills>();
    Set<Skills> skillsSet = new HashSet<Skills>();
    skillsSet.add(skills);
    Employee persistedEmployee =
        Employee.builder()
            .empId(longValue)
            .empSkillsSet(skillsSetEmpty)
            .givenName("abc")
            .familyName("xyz")
            .dateOfBirth("2016-04-01")
            .build();
    Optional<Employee> empOptional = Optional.of(persistedEmployee);

    Employee employeeWithSkillAdded =
        Employee.builder()
            .empId(longValue)
            .empSkillsSet(skillsSet)
            .givenName("abc")
            .familyName("xyz")
            .dateOfBirth("2016-04-01")
            .build();
    EmployeeResponse expectedResponse =
        EmployeeResponse.builder()
            .empId("1")
            .givenName("abc")
            .familyName("xyz")
            .dateOfBirth("2016-04-01")
            .skills(skillsSet)
            .responseStatus(HttpStatus.OK)
            .status(HttpStatus.OK.value())
            .message("Successfully added skill to the employee")
            .build();

    when(employeeRepository.findById(any(Long.class))).thenReturn(empOptional);
    when(employeeRepository.save(any(Employee.class))).thenReturn(employeeWithSkillAdded);
    when(skillsRepository.findById(any(Long.class))).thenReturn(skillOptional);

    EmployeeResponse employeeResponse =
        employeeRegistrationService.addSkillsToEmployee(longValue, longValue);

    assertEquals(employeeResponse, expectedResponse);
    verify(employeeRepository, times(1)).findById(longValue);
    verify(skillsRepository, times(1)).findById(longValue);
    verify(employeeRepository, times(1)).save(any(Employee.class));
  }

  @Test
  void test_addSkillsToEmployee_returnsEmpOrSkillNotFound_whenEmpIdAndSkillIdIsValid() {
    Long longValue = 1L;
    Optional<Skills> skillOptional = Optional.empty();
    Optional<Employee> empOptional = Optional.empty();

    EmployeeResponse expectedResponse =
        EmployeeResponse.builder()
            .responseStatus(HttpStatus.OK)
            .status(HttpStatus.OK.value())
            .message("The requested empId or skillId does not exist")
            .build();

    when(employeeRepository.findById(any(Long.class))).thenReturn(empOptional);
    when(skillsRepository.findById(any(Long.class))).thenReturn(skillOptional);

    EmployeeResponse employeeResponse =
        employeeRegistrationService.addSkillsToEmployee(longValue, longValue);

    assertEquals(employeeResponse, expectedResponse);
    verify(employeeRepository, times(1)).findById(longValue);
    verify(skillsRepository, times(1)).findById(longValue);
  }
}
