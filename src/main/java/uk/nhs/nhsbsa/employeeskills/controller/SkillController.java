package uk.nhs.nhsbsa.employeeskills.controller;

import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import uk.nhs.nhsbsa.employeeskills.request.SkillsRegistrationRequest;
import uk.nhs.nhsbsa.employeeskills.response.SkillsResponse;
import uk.nhs.nhsbsa.employeeskills.service.ISkillRegistrationService;

import javax.validation.Valid;
import javax.validation.constraints.Min;

@Api(tags = "Manage skills API")
@Slf4j
@RestController
@Validated
@RequestMapping("/skills")
public class SkillController {

  @Autowired private ISkillRegistrationService skillRegistrationService;

  @ApiOperation("Register a skill")
  @ApiResponses(
      value = {
        @ApiResponse(code = 201, message = "Success created the skill"),
        @ApiResponse(code = 400, message = "Bad Request"),
        @ApiResponse(code = 404, message = "Not Found"),
        @ApiResponse(code = 405, message = "Method Not Allowed"),
        @ApiResponse(code = 500, message = "Internal Server Error")
      })
  @PostMapping(consumes = "application/json", produces = "application/json")
  public ResponseEntity<SkillsResponse> registerSkills(
      final @Valid @RequestBody SkillsRegistrationRequest registrationRequest) {

    log.info("Request to register skills with skill name: {}", registrationRequest.getSkill());

    SkillsResponse response = skillRegistrationService.registerSkills(registrationRequest);

    return new ResponseEntity<>(response, response.getResponseStatus());
  }

  @ApiOperation("Fetch a specific skill details")
  @ApiResponses(
      value = {
        @ApiResponse(code = 200, message = "Successfully fetched data"),
        @ApiResponse(code = 400, message = "Bad Request"),
        @ApiResponse(code = 404, message = "Not Found"),
        @ApiResponse(code = 405, message = "Method Not Allowed"),
        @ApiResponse(code = 500, message = "Internal Server Error")
      })
  @GetMapping("/{skillId}")
  public ResponseEntity<SkillsResponse> fetchRegisteredSkill(
      final @ApiParam(value = "Accepts skillId", required = true) @PathVariable("skillId") @Min(1)
          Long skillId) {

    log.info("Request to fetching a skill with skillId: {}", skillId);

    SkillsResponse response = skillRegistrationService.fetchRegisteredSkill(skillId);

    return new ResponseEntity<>(response, response.getResponseStatus());
  }

  @ApiOperation("Delete an existing skill details")
  @ApiResponses(
      value = {
        @ApiResponse(code = 200, message = "Successfully deleted the skill"),
        @ApiResponse(code = 400, message = "Bad Request"),
        @ApiResponse(code = 404, message = "Not Found"),
        @ApiResponse(code = 405, message = "Method Not Allowed"),
        @ApiResponse(code = 500, message = "Internal Server Error")
      })
  @DeleteMapping("/{skillId}")
  public ResponseEntity<SkillsResponse> deleteRegisteredSkill(
      final @ApiParam(value = "Accepts skillId", required = true) @PathVariable("skillId") @Min(1)
          Long skillId) {

    log.info("Request to delete a skill with skillId: {}", skillId);

    SkillsResponse response = skillRegistrationService.deleteRegisteredSkill(skillId);

    return new ResponseEntity<>(response, response.getResponseStatus());
  }

  @ApiOperation("Update an existing skill details")
  @ApiResponses(
      value = {
        @ApiResponse(code = 200, message = "Successfully updated skill data"),
        @ApiResponse(code = 400, message = "Bad Request"),
        @ApiResponse(code = 404, message = "Not Found"),
        @ApiResponse(code = 405, message = "Method Not Allowed"),
        @ApiResponse(code = 500, message = "Internal Server Error")
      })
  @PutMapping("/{skillId}")
  public ResponseEntity<SkillsResponse> updateRegisteredSkills(
      final @Valid @RequestBody SkillsRegistrationRequest registrationRequest,
      final @ApiParam(value = "Accepts skillId", required = true) @PathVariable("skillId") @Min(1)
          Long skillId) {

    log.info("Update register skills for skillId: {}", registrationRequest.getSkill());

    SkillsResponse response =
        skillRegistrationService.updateRegisteredSkills(registrationRequest, skillId);

    return new ResponseEntity<>(response, response.getResponseStatus());
  }
}
