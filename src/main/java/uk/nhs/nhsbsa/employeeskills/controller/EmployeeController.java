package uk.nhs.nhsbsa.employeeskills.controller;

import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import uk.nhs.nhsbsa.employeeskills.request.EmployeeRegistrationRequest;
import uk.nhs.nhsbsa.employeeskills.response.EmployeeResponse;
import uk.nhs.nhsbsa.employeeskills.service.IEmployeeRegistrationService;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Api(tags = "Manage employee API")
@Slf4j
@RestController
@Validated
@RequestMapping("/employee")
public class EmployeeController {

  @Autowired private IEmployeeRegistrationService employeeRegistrationService;

  @ApiOperation("Register a employee")
  @ApiResponses(
      value = {
        @ApiResponse(code = 201, message = "Successfully registered"),
        @ApiResponse(code = 400, message = "Bad Request"),
        @ApiResponse(code = 404, message = "Not Found"),
        @ApiResponse(code = 405, message = "Method Not Allowed"),
        @ApiResponse(code = 500, message = "Internal Server Error")
      })
  @PostMapping(consumes = "application/json", produces = "application/json")
  public ResponseEntity<EmployeeResponse> registerEmployee(
      final @Valid @RequestBody EmployeeRegistrationRequest registrationRequest) {

    log.info("Request to register for emp:");

    EmployeeResponse response = employeeRegistrationService.registerEmployee(registrationRequest);

    return new ResponseEntity<>(response, response.getResponseStatus());
  }

  @ApiOperation("Fetch a specific employee details")
  @ApiResponses(
      value = {
        @ApiResponse(code = 200, message = "Successfully fetched data"),
        @ApiResponse(code = 400, message = "Bad Request"),
        @ApiResponse(code = 404, message = "Not Found"),
        @ApiResponse(code = 405, message = "Method Not Allowed"),
        @ApiResponse(code = 500, message = "Internal Server Error")
      })
  @GetMapping("/{empId}")
  public ResponseEntity<EmployeeResponse> fetchEmployeeDetails(
      final @ApiParam(value = "Accepts empId", required = true) @PathVariable("empId") @Min(1) @Max(
              2000) Long empId) {

    log.info("Request to fetch employee data for empId: {}", empId);

    EmployeeResponse response = employeeRegistrationService.fetchEmployeeDetails(empId);

    return new ResponseEntity<>(response, response.getResponseStatus());
  }

  @ApiOperation("Update an existing employee details")
  @ApiResponses(
      value = {
        @ApiResponse(code = 200, message = "Successfully updated employee data"),
        @ApiResponse(code = 400, message = "Bad Request"),
        @ApiResponse(code = 404, message = "Not Found"),
        @ApiResponse(code = 405, message = "Method Not Allowed"),
        @ApiResponse(code = 500, message = "Internal Server Error")
      })
  @PutMapping("/{empId}")
  public ResponseEntity<EmployeeResponse> updateEmployeeDetails(
      final @Valid @RequestBody EmployeeRegistrationRequest registrationRequest,
      final @ApiParam(value = "Accepts empId", required = true) @PathVariable("empId") @Min(1) @Max(
              2000) Long empId) {
    log.info("Request for updating employee data for empId: {}", empId);

    EmployeeResponse response =
        employeeRegistrationService.updateEmployeeDetails(empId, registrationRequest);

    return new ResponseEntity<>(response, response.getResponseStatus());
  }

  @ApiOperation("Delete an existing employee details")
  @ApiResponses(
      value = {
        @ApiResponse(code = 200, message = "Successfully deleted employee"),
        @ApiResponse(code = 400, message = "Bad Request"),
        @ApiResponse(code = 404, message = "Not Found"),
        @ApiResponse(code = 405, message = "Method Not Allowed"),
        @ApiResponse(code = 500, message = "Internal Server Error")
      })
  @DeleteMapping("/{empId}")
  public ResponseEntity<EmployeeResponse> deleteEmployeeDetails(
      final @ApiParam(value = "Accepts empId", required = true) @PathVariable("empId") @Min(1) @Max(
              2000) Long empId) {

    log.info("Request for deleting employee data for empId: {}", empId);

    EmployeeResponse response = employeeRegistrationService.deleteEmployeeDetails(empId);

    return new ResponseEntity<>(response, response.getResponseStatus());
  }

  @ApiOperation("Add skill to an existing employee details")
  @ApiResponses(
      value = {
        @ApiResponse(code = 200, message = "Successfully added skill to the employee"),
        @ApiResponse(code = 400, message = "Bad Request"),
        @ApiResponse(code = 404, message = "Not Found"),
        @ApiResponse(code = 405, message = "Method Not Allowed"),
        @ApiResponse(code = 500, message = "Internal Server Error")
      })
  @PutMapping("/{empId}/skills/{skillId}")
  public ResponseEntity<EmployeeResponse> addSkillsToEmployee(
      final @ApiParam(value = "Accepts empId", required = true) @PathVariable("empId") @Min(1) @Max(
              2000) Long empId,
      final @ApiParam(value = "Accepts skillId", required = true) @PathVariable("skillId") @Min(1)
          Long skillId) {

    log.info(
        "Request to add a skill with skillId: {} to the employee with empId: {}", skillId, empId);

    EmployeeResponse response = employeeRegistrationService.addSkillsToEmployee(empId, skillId);

    return new ResponseEntity<>(response, response.getResponseStatus());
  }

  @ApiOperation("Delete skill to an existing employee details")
  @ApiResponses(
      value = {
        @ApiResponse(code = 200, message = "Successfully deleted the skill"),
        @ApiResponse(code = 400, message = "Bad Request"),
        @ApiResponse(code = 404, message = "Not Found"),
        @ApiResponse(code = 405, message = "Method Not Allowed"),
        @ApiResponse(code = 500, message = "Internal Server Error")
      })
  @DeleteMapping("/{empId}/skills/{skillId}")
  public ResponseEntity<EmployeeResponse> deleteSkillFromEmployee(
      final @ApiParam(value = "Accepts empId", required = true) @PathVariable("empId") @Min(1) @Max(
              2000) Long empId,
      final @ApiParam(value = "Accepts skillId", required = true) @PathVariable("skillId") @Min(1)
          Long skillId) {

    log.info(
        "Request to delete a skill with skillId: {} from the employee with empId: {}",
        skillId,
        empId);

    EmployeeResponse response = employeeRegistrationService.deleteSkillFromEmployee(empId, skillId);

    return new ResponseEntity<>(response, response.getResponseStatus());
  }
}
