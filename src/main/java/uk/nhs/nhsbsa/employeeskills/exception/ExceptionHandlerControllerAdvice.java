package uk.nhs.nhsbsa.employeeskills.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import uk.nhs.nhsbsa.employeeskills.response.ErrorModel;
import uk.nhs.nhsbsa.employeeskills.response.ErrorResponse;

import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class ExceptionHandlerControllerAdvice {

  @ExceptionHandler(MethodArgumentNotValidException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ErrorResponse handleServiceException(final MethodArgumentNotValidException exception) {
    log.error("Method Argument Not Valid : ", exception);
    List<ErrorModel> errorMessages =
        exception.getBindingResult().getFieldErrors().stream()
            .map(
                err ->
                    new ErrorModel(err.getField(), err.getRejectedValue(), err.getDefaultMessage()))
            .distinct()
            .collect(Collectors.toList());
    return ErrorResponse.builder()
        .status(400)
        .errorInfo("BadRequestError")
        .fieldLevelErrorMessage(errorMessages)
        .build();
  }

  @ExceptionHandler({ConstraintViolationException.class})
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ErrorResponse constraintViolationException(final ConstraintViolationException exception) {
    log.error("Constraint Violation & number format Exception : ", exception);
    return ErrorResponse.builder().status(400).errorInfo("Bad Request").build();
  }

  @ExceptionHandler({HttpRequestMethodNotSupportedException.class})
  @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
  public ErrorResponse httpRequestMethodNotSupportedException(
      final HttpRequestMethodNotSupportedException exception) {
    log.error("Method Not Allowed : ", exception);
    return ErrorResponse.builder().status(405).errorInfo("Method Not Allowed").build();
  }

  @ExceptionHandler({NumberFormatException.class})
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ErrorResponse numberFormatException(final NumberFormatException exception) {
    log.error("Number format Exception : ", exception);
    return ErrorResponse.builder()
        .status(400)
        .errorInfo("Bad Request: " + exception.getMessage())
        .build();
  }
}
