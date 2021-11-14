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
public class EmployeeRegistrationRequest {

  @JsonProperty("givenName")
  @NotBlank(message = "{givenName.not-null}")
  @Pattern(regexp = "^[A-Za-z0-9-]*$")
  private String givenName;

  @JsonProperty("familyName")
  @NotBlank(message = "{familyName.not-null}")
  @Pattern(regexp = "^[A-Za-z0-9-]*$")
  private String familyName;

  @JsonProperty("dateOfBirth")
  @NotBlank(message = "{dateOfBirth.not-null}")
  @Pattern(regexp = "^[0-9]{4}-(0[1-9]|1[012])-(0[1-9]|[12][0-9]|3[01])$")
  private String dateOfBirth;
}
