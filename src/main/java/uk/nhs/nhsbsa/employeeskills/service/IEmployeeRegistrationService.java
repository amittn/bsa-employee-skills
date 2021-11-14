package uk.nhs.nhsbsa.employeeskills.service;

import uk.nhs.nhsbsa.employeeskills.request.EmployeeRegistrationRequest;
import uk.nhs.nhsbsa.employeeskills.response.EmployeeResponse;

public interface IEmployeeRegistrationService {

  EmployeeResponse registerEmployee(EmployeeRegistrationRequest employeeRegistrationRequest);

  EmployeeResponse fetchEmployeeDetails(Long empId);

  EmployeeResponse updateEmployeeDetails(
      Long empId, EmployeeRegistrationRequest registrationRequest);

  EmployeeResponse deleteEmployeeDetails(Long empId);

  EmployeeResponse deleteSkillFromEmployee(Long empId, Long skillId);

  EmployeeResponse addSkillsToEmployee(Long empId, Long skillId);
}
