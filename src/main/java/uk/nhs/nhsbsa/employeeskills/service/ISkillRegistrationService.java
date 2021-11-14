package uk.nhs.nhsbsa.employeeskills.service;

import uk.nhs.nhsbsa.employeeskills.request.SkillsRegistrationRequest;
import uk.nhs.nhsbsa.employeeskills.response.SkillsResponse;

public interface ISkillRegistrationService {

  SkillsResponse registerSkills(SkillsRegistrationRequest registrationRequest);

  SkillsResponse fetchRegisteredSkill(Long skillId);

  SkillsResponse deleteRegisteredSkill(Long skillId);

  SkillsResponse updateRegisteredSkills(
      SkillsRegistrationRequest registrationRequest, Long skillId);
}
