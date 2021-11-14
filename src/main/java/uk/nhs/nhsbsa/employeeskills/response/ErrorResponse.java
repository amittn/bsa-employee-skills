package uk.nhs.nhsbsa.employeeskills.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse {
  private int status;
  private String errorInfo;
  private List<ErrorModel> fieldLevelErrorMessage;
}
