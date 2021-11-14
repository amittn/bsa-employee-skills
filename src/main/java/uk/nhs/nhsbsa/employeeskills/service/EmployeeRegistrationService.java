package uk.nhs.nhsbsa.employeeskills.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import uk.nhs.nhsbsa.employeeskills.entity.Employee;
import uk.nhs.nhsbsa.employeeskills.entity.Skills;
import uk.nhs.nhsbsa.employeeskills.repository.EmployeeRepository;
import uk.nhs.nhsbsa.employeeskills.repository.SkillsRepository;
import uk.nhs.nhsbsa.employeeskills.request.EmployeeRegistrationRequest;
import uk.nhs.nhsbsa.employeeskills.response.EmployeeResponse;

import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
public class EmployeeRegistrationService implements IEmployeeRegistrationService {

  public static final String THE_REQUESTED_EMP_ID_DOES_NOT_EXIST =
      "The requested empId does not exist";
  @Autowired private EmployeeRepository employeeRepository;

  @Autowired private SkillsRepository skillsRepository;

  @Override
  public EmployeeResponse registerEmployee(
      final EmployeeRegistrationRequest employeeRegistrationRequest) {
    log.info("Register employee");

    String empId = UUID.randomUUID().toString();

    Employee employee =
        Employee.builder()
            .checkSum(empId)
            .givenName(employeeRegistrationRequest.getGivenName())
            .familyName(employeeRegistrationRequest.getFamilyName())
            .dateOfBirth(employeeRegistrationRequest.getDateOfBirth())
            .build();

    Employee persistedEmp = employeeRepository.save(employee);

    return EmployeeResponse.builder()
        .empId(persistedEmp.getEmpId().toString())
        .familyName(persistedEmp.getFamilyName())
        .givenName(persistedEmp.getGivenName())
        .dateOfBirth(persistedEmp.getDateOfBirth())
        .skills(persistedEmp.getEmpSkillsSet())
        .message("Successfully registered")
        .status(HttpStatus.CREATED.value())
        .responseStatus(HttpStatus.CREATED)
        .build();
  }

  @Override
  public EmployeeResponse fetchEmployeeDetails(final Long empId) {

    log.info("fetching employee data for empId : {}", empId);

    Optional<Employee> persistedEmp = employeeRepository.findById(empId);

    if (persistedEmp.isPresent()) {
      Employee employee = persistedEmp.get();
      return EmployeeResponse.builder()
          .skills(employee.getEmpSkillsSet())
          .givenName(employee.getGivenName())
          .familyName(employee.getFamilyName())
          .dateOfBirth(employee.getDateOfBirth())
          .empId(employee.getEmpId().toString())
          .responseStatus(HttpStatus.OK)
          .status(HttpStatus.OK.value())
          .message("Successfully fetched data")
          .build();
    }
    return EmployeeResponse.builder()
        .status(HttpStatus.OK.value())
        .responseStatus(HttpStatus.OK)
        .message(THE_REQUESTED_EMP_ID_DOES_NOT_EXIST)
        .build();
  }

  @Override
  public EmployeeResponse updateEmployeeDetails(
      final Long empId, final EmployeeRegistrationRequest registrationRequest) {

    log.info("Updating employee with empId: {}", empId);

    Optional<Employee> persistedEmp = employeeRepository.findById(empId);

    if (persistedEmp.isPresent()) {
      Employee employee = persistedEmp.get();

      employee.setGivenName(registrationRequest.getGivenName());
      employee.setFamilyName(registrationRequest.getFamilyName());
      employee.setDateOfBirth(registrationRequest.getDateOfBirth());
      employeeRepository.save(employee);
      return EmployeeResponse.builder()
          .status(HttpStatus.OK.value())
          .responseStatus(HttpStatus.OK)
          .message("Successfully updated employee data")
          .build();
    }
    return EmployeeResponse.builder()
        .status(HttpStatus.OK.value())
        .responseStatus(HttpStatus.OK)
        .message(THE_REQUESTED_EMP_ID_DOES_NOT_EXIST)
        .build();
  }

  @Override
  public EmployeeResponse deleteEmployeeDetails(final Long empId) {

    log.info("deleting employee with empId: {}", empId);

    Optional<Employee> persistedEmp = employeeRepository.findById(empId);

    if (persistedEmp.isPresent()) {
      employeeRepository.deleteById(empId);
      return EmployeeResponse.builder()
          .message("Successfully deleted employee")
          .status(HttpStatus.OK.value())
          .responseStatus(HttpStatus.OK)
          .build();
    }

    return EmployeeResponse.builder()
        .message(THE_REQUESTED_EMP_ID_DOES_NOT_EXIST)
        .responseStatus(HttpStatus.OK)
        .status(HttpStatus.OK.value())
        .build();
  }

  @Override
  public EmployeeResponse deleteSkillFromEmployee(final Long empId, final Long skillId) {

    log.info("deleting a skill with skillId: {} to the employee with empId: {}", skillId, empId);

    Optional<Employee> fetchedEmp = employeeRepository.findById(empId);
    Optional<Skills> fetchedSkill = skillsRepository.findById(skillId);

    if (fetchedEmp.isPresent() && fetchedSkill.isPresent()) {
      Employee employee = fetchedEmp.get();
      Skills skills = fetchedSkill.get();

      if (employee.getEmpSkillsSet().contains(skills)) {
        employee.getEmpSkillsSet().remove(skills);
        Employee persistedEmp = employeeRepository.save(employee);

        return EmployeeResponse.builder()
            .givenName(persistedEmp.getGivenName())
            .familyName(persistedEmp.getFamilyName())
            .dateOfBirth(persistedEmp.getDateOfBirth())
            .skills(persistedEmp.getEmpSkillsSet())
            .empId(persistedEmp.getEmpId().toString())
            .status(HttpStatus.OK.value())
            .responseStatus(HttpStatus.OK)
            .message("Successfully deleted the skill")
            .build();
      }
      return EmployeeResponse.builder()
          .message("The requested empId doesn't have this skill")
          .responseStatus(HttpStatus.OK)
          .status(HttpStatus.OK.value())
          .build();
    }
    return EmployeeResponse.builder()
        .message("The requested empId or skillId does not exist")
        .responseStatus(HttpStatus.OK)
        .status(HttpStatus.OK.value())
        .build();
  }

  @Override
  public EmployeeResponse addSkillsToEmployee(final Long empId, final Long skillId) {

    log.info("adding a skill with skillId: {} to the employee with empId: {}", skillId, empId);

    Optional<Employee> fetchedEmp = employeeRepository.findById(empId);
    Optional<Skills> fetchedSkills = skillsRepository.findById(skillId);

    if (fetchedEmp.isPresent() && fetchedSkills.isPresent()) {
      Employee employee = fetchedEmp.get();
      employee.getEmpSkillsSet().add(fetchedSkills.get());
      Employee persistedEmp = employeeRepository.save(employee);

      return EmployeeResponse.builder()
          .givenName(persistedEmp.getGivenName())
          .familyName(persistedEmp.getFamilyName())
          .dateOfBirth(persistedEmp.getDateOfBirth())
          .skills(persistedEmp.getEmpSkillsSet())
          .empId(persistedEmp.getEmpId().toString())
          .message("Successfully added skill to the employee")
          .responseStatus(HttpStatus.OK)
          .status(HttpStatus.OK.value())
          .build();
    }
    return EmployeeResponse.builder()
        .message("The requested empId or skillId does not exist")
        .responseStatus(HttpStatus.OK)
        .status(HttpStatus.OK.value())
        .build();
  }
}
