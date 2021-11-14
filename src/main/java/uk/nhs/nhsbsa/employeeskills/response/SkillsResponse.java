package uk.nhs.nhsbsa.employeeskills.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import uk.nhs.nhsbsa.employeeskills.entity.Employee;

import java.util.HashSet;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SkillsResponse {

  @JsonProperty("skill")
  private String skill;

  @JsonProperty("level")
  private String level;

  @JsonProperty("skillId")
  private String skillId;

  @JsonProperty("empSkills")
  Set<Employee> empSkills = new HashSet<>();

  @JsonIgnore private HttpStatus responseStatus;

  @JsonProperty("message")
  private String message;

  @JsonProperty("status")
  private int status;
}
