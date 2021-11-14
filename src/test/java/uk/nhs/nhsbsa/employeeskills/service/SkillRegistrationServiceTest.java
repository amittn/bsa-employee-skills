package uk.nhs.nhsbsa.employeeskills.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import uk.nhs.nhsbsa.employeeskills.entity.Employee;
import uk.nhs.nhsbsa.employeeskills.entity.Skills;
import uk.nhs.nhsbsa.employeeskills.repository.SkillsRepository;
import uk.nhs.nhsbsa.employeeskills.request.SkillsRegistrationRequest;
import uk.nhs.nhsbsa.employeeskills.response.SkillsResponse;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest
class SkillRegistrationServiceTest {

  @MockBean private SkillsRepository skillsRepository;

  @Autowired private SkillRegistrationService skillRegistrationService;

  @Test
  void test_registerSkills_returnsSkillsResponse_whenRequestIsValid() {
    SkillsRegistrationRequest skillsRegistrationRequest =
        SkillsRegistrationRequest.builder().skill("abc").level("Working").build();
    Skills persistedSkills = Skills.builder().skillId(1L).skill("abc").level("Working").build();
    SkillsResponse expectedResponse =
        SkillsResponse.builder()
            .skillId("1")
            .message("Success created the skill")
            .status(HttpStatus.CREATED.value())
            .responseStatus(HttpStatus.CREATED)
            .skill("abc")
            .level("Working")
            .build();

    when(skillsRepository.save(any(Skills.class))).thenReturn(persistedSkills);

    SkillsResponse response = skillRegistrationService.registerSkills(skillsRegistrationRequest);

    assertEquals(response, expectedResponse);
    verify(skillsRepository, times(1)).save(any(Skills.class));
  }

  @Test
  void test_fetchRegisteredSkill_returnsSkillsResponse_whenSkillIdValid() {
    Skills persistedSkills = Skills.builder().skillId(1L).skill("abc").level("Working").build();
    Optional<Skills> skillOptional = Optional.of(persistedSkills);
    SkillsResponse expectedResponse =
        SkillsResponse.builder()
            .skillId("1")
            .message("Successfully fetched data")
            .status(HttpStatus.OK.value())
            .responseStatus(HttpStatus.OK)
            .skill("abc")
            .level("Working")
            .build();

    when(skillsRepository.findById(any(Long.class))).thenReturn(skillOptional);

    SkillsResponse response = skillRegistrationService.fetchRegisteredSkill(1L);

    assertEquals(response, expectedResponse);
    verify(skillsRepository, times(1)).findById(any(Long.class));
  }

  @Test
  void test_fetchRegisteredSkill_returnsSkillsNotFound_whenSkillIdValid() {
    Optional<Skills> skillOptional = Optional.empty();
    SkillsResponse expectedResponse =
        SkillsResponse.builder()
            .message("The requested skillId does not exist")
            .status(HttpStatus.OK.value())
            .responseStatus(HttpStatus.OK)
            .build();

    when(skillsRepository.findById(any(Long.class))).thenReturn(skillOptional);

    SkillsResponse response = skillRegistrationService.fetchRegisteredSkill(1L);

    assertEquals(response, expectedResponse);
    verify(skillsRepository, times(1)).findById(any(Long.class));
  }

  @Test
  void test_deleteRegisteredSkill_returnsSkillsResponse_whenSkillIdValid() {
    Skills persistedSkills =
        Skills.builder().empSet(new HashSet<>()).skillId(1L).skill("abc").level("Working").build();
    Optional<Skills> skillOptional = Optional.of(persistedSkills);
    SkillsResponse expectedResponse =
        SkillsResponse.builder()
            .message("Successfully deleted the skill")
            .status(HttpStatus.OK.value())
            .responseStatus(HttpStatus.OK)
            .build();

    when(skillsRepository.findById(any(Long.class))).thenReturn(skillOptional);

    SkillsResponse response = skillRegistrationService.deleteRegisteredSkill(1L);

    assertEquals(response, expectedResponse);
    verify(skillsRepository, times(1)).findById(any(Long.class));
  }

  @Test
  void test_deleteRegisteredSkill_returnsCantDelete_whenSkillIdValid() {
    Set<Employee> employeeSet = new HashSet<>();
    employeeSet.add(Employee.builder().empId(1L).build());
    Skills persistedSkills =
        Skills.builder().empSet(employeeSet).skillId(1L).skill("abc").level("Working").build();
    Optional<Skills> skillOptional = Optional.of(persistedSkills);
    SkillsResponse expectedResponse =
        SkillsResponse.builder()
            .message("Can't deleted the skill as is used by a employee")
            .status(HttpStatus.OK.value())
            .responseStatus(HttpStatus.OK)
            .build();

    when(skillsRepository.findById(any(Long.class))).thenReturn(skillOptional);

    SkillsResponse response = skillRegistrationService.deleteRegisteredSkill(1L);

    assertEquals(response, expectedResponse);
    verify(skillsRepository, times(1)).findById(any(Long.class));
  }

  @Test
  void test_deleteRegisteredSkill_returnsSkillNotFound_whenSkillIdValid() {
    Optional<Skills> skillOptional = Optional.empty();
    SkillsResponse expectedResponse =
        SkillsResponse.builder()
            .message("The requested skillId does not exist")
            .status(HttpStatus.OK.value())
            .responseStatus(HttpStatus.OK)
            .build();

    when(skillsRepository.findById(any(Long.class))).thenReturn(skillOptional);

    SkillsResponse response = skillRegistrationService.deleteRegisteredSkill(1L);

    assertEquals(response, expectedResponse);
    verify(skillsRepository, times(1)).findById(any(Long.class));
  }

  @Test
  void test_updateRegisteredSkills_returnsSkillsResponse_whenRequestAndSkillIdValid() {
    SkillsRegistrationRequest skillsRegistrationRequest =
        SkillsRegistrationRequest.builder().skill("abc").level("Working").build();
    Skills persistedSkills = Skills.builder().skillId(1L).skill("abc").level("Working").build();
    Optional<Skills> skillOptional = Optional.of(persistedSkills);
    SkillsResponse expectedResponse =
        SkillsResponse.builder()
            .message("Successfully updated skill data")
            .status(HttpStatus.OK.value())
            .responseStatus(HttpStatus.OK)
            .build();

    when(skillsRepository.findById(any(Long.class))).thenReturn(skillOptional);
    when(skillsRepository.save(any(Skills.class))).thenReturn(any());

    SkillsResponse response =
        skillRegistrationService.updateRegisteredSkills(skillsRegistrationRequest, 1L);

    assertEquals(response, expectedResponse);
    verify(skillsRepository, times(1)).findById(1L);
  }

  @Test
  void test_updateRegisteredSkills_returnsSkillNotFound_whenSkillIdValid() {
    SkillsRegistrationRequest skillsRegistrationRequest =
        SkillsRegistrationRequest.builder().skill("abc").level("Working").build();
    Optional<Skills> skillOptional = Optional.empty();
    SkillsResponse expectedResponse =
        SkillsResponse.builder()
            .message("The requested skillId does not exist")
            .status(HttpStatus.OK.value())
            .responseStatus(HttpStatus.OK)
            .build();

    when(skillsRepository.findById(any(Long.class))).thenReturn(skillOptional);

    SkillsResponse response =
        skillRegistrationService.updateRegisteredSkills(skillsRegistrationRequest, 1L);

    assertEquals(response, expectedResponse);
    verify(skillsRepository, times(1)).findById(1L);
  }
}
