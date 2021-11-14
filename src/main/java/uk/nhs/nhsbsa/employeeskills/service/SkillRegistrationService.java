package uk.nhs.nhsbsa.employeeskills.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import uk.nhs.nhsbsa.employeeskills.entity.Skills;
import uk.nhs.nhsbsa.employeeskills.repository.SkillsRepository;
import uk.nhs.nhsbsa.employeeskills.request.SkillsRegistrationRequest;
import uk.nhs.nhsbsa.employeeskills.response.SkillsResponse;

import java.util.Optional;

@Service
@Slf4j
public class SkillRegistrationService implements ISkillRegistrationService {

  public static final String THE_REQUESTED_SKILL_ID_DOES_NOT_EXIST =
      "The requested skillId does not exist";

  @Autowired private SkillsRepository skillsRepository;

  @Override
  public SkillsResponse registerSkills(final SkillsRegistrationRequest registrationRequest) {
    log.info("Registering skills with sill name: {}", registrationRequest.getSkill());

    Skills skillsRequested =
        Skills.builder()
            .skill(registrationRequest.getSkill())
            .level(registrationRequest.getLevel())
            .build();

    Skills persistedSkill = skillsRepository.save(skillsRequested);

    return SkillsResponse.builder()
        .skillId(persistedSkill.getSkillId().toString())
        .skill(persistedSkill.getSkill())
        .level(persistedSkill.getLevel())
        .empSkills(persistedSkill.getEmpSet())
        .message("Success created the skill")
        .status(HttpStatus.CREATED.value())
        .responseStatus(HttpStatus.CREATED)
        .build();
  }

  @Override
  public SkillsResponse fetchRegisteredSkill(final Long skillId) {
    log.info("Trying to fetching a skill with skillId: {}", skillId);

    Optional<Skills> fetchedSkills = skillsRepository.findById(skillId);

    if (fetchedSkills.isPresent()) {
      Skills skills = fetchedSkills.get();
      return SkillsResponse.builder()
          .skill(skills.getSkill())
          .level(skills.getLevel())
          .skillId(skills.getSkillId().toString())
          .message("Successfully fetched data")
          .responseStatus(HttpStatus.OK)
          .status(HttpStatus.OK.value())
          .build();
    }
    return SkillsResponse.builder()
        .message(THE_REQUESTED_SKILL_ID_DOES_NOT_EXIST)
        .status(200)
        .responseStatus(HttpStatus.OK)
        .build();
  }

  @Override
  public SkillsResponse deleteRegisteredSkill(final Long skillId) {
    log.info("Trying to delete skill with skillId: {}", skillId);

    Optional<Skills> fetchedSkills = skillsRepository.findById(skillId);
    String message = THE_REQUESTED_SKILL_ID_DOES_NOT_EXIST;

    if (fetchedSkills.isPresent()) {
      Skills skills = fetchedSkills.get();
      if (skills.getEmpSet().isEmpty()) {
        skillsRepository.deleteById(skillId);
        message = "Successfully deleted the skill";
      } else {
        message = "Can't deleted the skill as is used by a employee";
      }
    }
    return SkillsResponse.builder()
        .message(message)
        .status(HttpStatus.OK.value())
        .responseStatus(HttpStatus.OK)
        .build();
  }

  @Override
  public SkillsResponse updateRegisteredSkills(
      final SkillsRegistrationRequest registrationRequest, Long skillId) {
    log.info("Updating skill with skillId: {}", skillId);

    Optional<Skills> fetchedSkillId = skillsRepository.findById(skillId);
    String message = THE_REQUESTED_SKILL_ID_DOES_NOT_EXIST;

    if (fetchedSkillId.isPresent()) {
      Skills skill = fetchedSkillId.get();
      skill.setLevel(registrationRequest.getLevel());
      skill.setSkill(registrationRequest.getSkill());
      skillsRepository.save(skill);
      message = "Successfully updated skill data";
    }
    return SkillsResponse.builder()
        .status(200)
        .message(message)
        .responseStatus(HttpStatus.OK)
        .build();
  }
}
