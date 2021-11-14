package uk.nhs.nhsbsa.employeeskills.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SkillsRegistrationRequest {

  @JsonProperty("skill")
  @NotBlank(message = "{skill.not-null}")
  @Pattern(regexp = "^[A-Za-z0-9-]*$")
  private String skill;

  @JsonProperty("level")
  @NotBlank(message = "{level.not-null}")
  @Pattern(regexp = "^$|Expert|Practitioner|Working|Awareness", message = "{level.pattern}")
  private String level;
}
