package uk.nhs.nhsbsa.employeeskills.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import uk.nhs.nhsbsa.employeeskills.request.EmployeeRegistrationRequest;
import uk.nhs.nhsbsa.employeeskills.response.EmployeeResponse;
import uk.nhs.nhsbsa.employeeskills.service.EmployeeRegistrationService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class EmployeeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmployeeRegistrationService employeeRegistrationService;

    @Test
    void test_registerEmployee_returns200_whenRequestObjectIsValid() throws Exception {
        EmployeeRegistrationRequest request =
                EmployeeRegistrationRequest.builder()
                        .dateOfBirth("2020-02-02")
                        .familyName("abcdef")
                        .givenName("ghijk")
                        .build();

        EmployeeResponse response =
                EmployeeResponse.builder()
                        .responseStatus(HttpStatus.CREATED)
                        .empId("1")
                        .status(HttpStatus.CREATED.value())
                        .givenName("ghijk")
                        .familyName("abcdef")
                        .message("i am in")
                        .dateOfBirth("2020-02-02")
                        .build();

        when(employeeRegistrationService.registerEmployee(request)).thenReturn(response);
        mockMvc
                .perform(
                        post("/employee")
                                .header("Content-Type", "application/json")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(asJsonString(request)))
                .andExpect(handler().handlerType(EmployeeController.class))
                .andExpect(handler().methodName("registerEmployee"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value("i am in"))
                .andExpect(jsonPath("$.status").value(201))
                .andExpect(jsonPath("$.dateOfBirth").value("2020-02-02"))
                .andExpect(jsonPath("$.familyName").value("abcdef"))
                .andExpect(jsonPath("$.givenName").value("ghijk"));

        verify(employeeRegistrationService, times(1)).registerEmployee(request);
        verifyNoMoreInteractions(employeeRegistrationService);
    }

    @Test
    void test_registerEmployee_returns400_whenInValidDateOfBirth() throws Exception {
        EmployeeRegistrationRequest request =
                EmployeeRegistrationRequest.builder()
                        .dateOfBirth("20208-02-02")
                        .familyName("abc")
                        .givenName("xyz")
                        .build();

        mockMvc
                .perform(
                        post("/employee")
                                .header("Content-Type", "application/json")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(asJsonString(request)))
                .andExpect(handler().handlerType(EmployeeController.class))
                .andExpect(handler().methodName("registerEmployee"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.errorInfo").value("BadRequestError"))
                .andExpect(jsonPath("$.fieldLevelErrorMessage[0].fieldName").value("dateOfBirth"));

        verify(employeeRegistrationService, times(0)).registerEmployee(request);
        verifyNoMoreInteractions(employeeRegistrationService);
    }

    @Test
    void test_registerEmployee_returns400_whenInValidFamilyName() throws Exception {
        EmployeeRegistrationRequest request =
                EmployeeRegistrationRequest.builder()
                        .dateOfBirth("2020-02-02")
                        .familyName("ab$c")
                        .givenName("xyz")
                        .build();

        mockMvc
                .perform(
                        post("/employee")
                                .header("Content-Type", "application/json")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(asJsonString(request)))
                .andExpect(handler().handlerType(EmployeeController.class))
                .andExpect(handler().methodName("registerEmployee"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.errorInfo").value("BadRequestError"))
                .andExpect(jsonPath("$.fieldLevelErrorMessage[0].fieldName").value("familyName"));

        verify(employeeRegistrationService, times(0)).registerEmployee(request);
        verifyNoMoreInteractions(employeeRegistrationService);
    }

    @Test
    void test_registerEmployee_returns400_whenInValidGivenName() throws Exception {
        EmployeeRegistrationRequest request =
                EmployeeRegistrationRequest.builder()
                        .dateOfBirth("2020-02-02")
                        .familyName("abc")
                        .givenName("x£yz")
                        .build();

        mockMvc
                .perform(
                        post("/employee")
                                .header("Content-Type", "application/json")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(asJsonString(request)))
                .andExpect(handler().handlerType(EmployeeController.class))
                .andExpect(handler().methodName("registerEmployee"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.errorInfo").value("BadRequestError"))
                .andExpect(jsonPath("$.fieldLevelErrorMessage[0].fieldName").value("givenName"));

        verify(employeeRegistrationService, times(0)).registerEmployee(request);
        verifyNoMoreInteractions(employeeRegistrationService);
    }

    @Test
    void test_fetchEmployeeDetails_returns200_whenEmpIdIsValid() throws Exception {

        EmployeeResponse response =
                EmployeeResponse.builder()
                        .responseStatus(HttpStatus.OK)
                        .empId("1")
                        .status(HttpStatus.OK.value())
                        .givenName("ghijk")
                        .familyName("abcdef")
                        .message("i am in")
                        .dateOfBirth("2020-02-02")
                        .build();

        when(employeeRegistrationService.fetchEmployeeDetails(1L)).thenReturn(response);
        mockMvc
                .perform(get("/employee/{empId}", "1"))
                .andExpect(handler().handlerType(EmployeeController.class))
                .andExpect(handler().methodName("fetchEmployeeDetails"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("i am in"))
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.dateOfBirth").value("2020-02-02"))
                .andExpect(jsonPath("$.familyName").value("abcdef"))
                .andExpect(jsonPath("$.givenName").value("ghijk"));

        verify(employeeRegistrationService, times(1)).fetchEmployeeDetails(1L);
        verifyNoMoreInteractions(employeeRegistrationService);
    }

    @Test
    void test_fetchEmployeeDetails_returns400_whenPathVariableEmpIdIsInValid() throws Exception {

        EmployeeResponse response =
                EmployeeResponse.builder()
                        .responseStatus(HttpStatus.OK)
                        .empId("1")
                        .status(HttpStatus.OK.value())
                        .givenName("ghijk")
                        .familyName("abcdef")
                        .message("i am in")
                        .dateOfBirth("2020-02-02")
                        .build();

        when(employeeRegistrationService.fetchEmployeeDetails(1L)).thenReturn(response);
        MvcResult result = this.mockMvc
                .perform(get("/employee/{empId}", "1o"))
                .andExpect(handler().handlerType(EmployeeController.class))
                .andExpect(handler().methodName("fetchEmployeeDetails"))
                .andExpect(status().isBadRequest())
                .andReturn();

        String content = result.getResponse().getContentAsString();

        assertEquals(content, "{\"status\":400,\"errorInfo\":\"Bad Request: For input string: \\\"1o\\\"\"}");

        verify(employeeRegistrationService, times(0)).fetchEmployeeDetails(1L);
        verifyNoMoreInteractions(employeeRegistrationService);
    }

    @Test
    void test_fetchEmployeeDetails_returns405_whenPathVariableEmpIdIsBlank() throws Exception {

        EmployeeResponse response =
                EmployeeResponse.builder()
                        .responseStatus(HttpStatus.OK)
                        .empId("1")
                        .status(HttpStatus.OK.value())
                        .givenName("ghijk")
                        .familyName("abcdef")
                        .message("i am in")
                        .dateOfBirth("2020-02-02")
                        .build();

        when(employeeRegistrationService.fetchEmployeeDetails(1L)).thenReturn(response);
        MvcResult result = this.mockMvc
                .perform(get("/employee/{empId}", ""))
                .andExpect(status().isMethodNotAllowed())
                .andReturn();

        String content = result.getResponse().getContentAsString();

        assertEquals(content, "{\"status\":405,\"errorInfo\":\"Method Not Allowed\"}");

        verify(employeeRegistrationService, times(0)).fetchEmployeeDetails(1L);
        verifyNoMoreInteractions(employeeRegistrationService);
    }

    @Test
    void test_updateEmployeeDetails_returns200_whenRequestObjectAndEmpIdIsValid() throws Exception {
        EmployeeRegistrationRequest request =
                EmployeeRegistrationRequest.builder()
                        .dateOfBirth("2020-02-02")
                        .familyName("abcdef")
                        .givenName("ghijk")
                        .build();

        EmployeeResponse response =
                EmployeeResponse.builder()
                        .responseStatus(HttpStatus.OK)
                        .empId("1")
                        .status(HttpStatus.OK.value())
                        .givenName("ghijk")
                        .familyName("abcdef")
                        .message("i am in")
                        .dateOfBirth("2020-02-02")
                        .build();

        when(employeeRegistrationService.updateEmployeeDetails(1L, request)).thenReturn(response);
        mockMvc
                .perform(
                        put("/employee/{empId}", "1")
                                .header("Content-Type", "application/json")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(asJsonString(request)))
                .andExpect(handler().handlerType(EmployeeController.class))
                .andExpect(handler().methodName("updateEmployeeDetails"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("i am in"))
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.dateOfBirth").value("2020-02-02"))
                .andExpect(jsonPath("$.familyName").value("abcdef"))
                .andExpect(jsonPath("$.givenName").value("ghijk"));

        verify(employeeRegistrationService, times(1)).updateEmployeeDetails(1L, request);
        verifyNoMoreInteractions(employeeRegistrationService);
    }

    @Test
    void test_updateEmployeeDetails_returns400_whenURLIdIsValid() throws Exception {
        EmployeeRegistrationRequest request =
                EmployeeRegistrationRequest.builder()
                        .dateOfBirth("2020-02-02")
                        .familyName("abcdef")
                        .givenName("ghijk")
                        .build();
        EmployeeResponse response =
                EmployeeResponse.builder()
                        .responseStatus(HttpStatus.OK)
                        .empId("1")
                        .status(HttpStatus.OK.value())
                        .givenName("ghijk")
                        .familyName("abcdef")
                        .message("i am in")
                        .dateOfBirth("2020-02-02")
                        .build();

        when(employeeRegistrationService.updateEmployeeDetails(1L, request)).thenReturn(response);
        mockMvc
                .perform(put("/employee/{empId}", "1p"))
                .andExpect(handler().handlerType(EmployeeController.class))
                .andExpect(handler().methodName("updateEmployeeDetails"))
                .andExpect(status().isBadRequest());

        verify(employeeRegistrationService, times(0)).updateEmployeeDetails(1L, request);
        verifyNoMoreInteractions(employeeRegistrationService);
    }

    @Test
    void test_updateEmployeeDetails_returns405_whenPathVariableIsBlank() throws Exception {
        EmployeeRegistrationRequest request =
                EmployeeRegistrationRequest.builder()
                        .dateOfBirth("2020-02-02")
                        .familyName("abcdef")
                        .givenName("ghijk")
                        .build();
        EmployeeResponse response =
                EmployeeResponse.builder()
                        .responseStatus(HttpStatus.OK)
                        .empId("1")
                        .status(HttpStatus.OK.value())
                        .givenName("ghijk")
                        .familyName("abcdef")
                        .message("i am in")
                        .dateOfBirth("2020-02-02")
                        .build();

        when(employeeRegistrationService.updateEmployeeDetails(1L, request)).thenReturn(response);
        MvcResult result = this.mockMvc
                .perform(put("/employee/{empId}", ""))
                .andExpect(status().isMethodNotAllowed())
                .andReturn();

        String content = result.getResponse().getContentAsString();

        assertEquals(content, "{\"status\":405,\"errorInfo\":\"Method Not Allowed\"}");

        verify(employeeRegistrationService, times(0)).updateEmployeeDetails(1L, request);
        verifyNoMoreInteractions(employeeRegistrationService);
    }

    @Test
    void test_deleteEmployeeDetails_returns200_whenEmpIdIsValid() throws Exception {

        EmployeeResponse response =
                EmployeeResponse.builder()
                        .responseStatus(HttpStatus.OK)
                        .empId("1")
                        .status(HttpStatus.OK.value())
                        .givenName("ghijk")
                        .familyName("abcdef")
                        .message("i am in")
                        .dateOfBirth("2020-02-02")
                        .build();

        when(employeeRegistrationService.deleteEmployeeDetails(1L)).thenReturn(response);
        mockMvc
                .perform(delete("/employee/{empId}", "1"))
                .andExpect(handler().handlerType(EmployeeController.class))
                .andExpect(handler().methodName("deleteEmployeeDetails"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("i am in"))
                .andExpect(jsonPath("$.status").value(200));

        verify(employeeRegistrationService, times(1)).deleteEmployeeDetails(1L);
        verifyNoMoreInteractions(employeeRegistrationService);
    }

    @Test
    void test_deleteEmployeeDetails_returns400_whenEmpIdIsValid() throws Exception {

        EmployeeResponse response =
                EmployeeResponse.builder()
                        .responseStatus(HttpStatus.OK)
                        .empId("1")
                        .status(HttpStatus.OK.value())
                        .givenName("ghijk")
                        .familyName("abcdef")
                        .message("i am in")
                        .dateOfBirth("2020-02-02")
                        .build();

        when(employeeRegistrationService.deleteEmployeeDetails(1L)).thenReturn(response);
        MvcResult result = this.mockMvc
                .perform(delete("/employee/{empId}", "1@"))
                .andExpect(handler().handlerType(EmployeeController.class))
                .andExpect(handler().methodName("deleteEmployeeDetails"))
                .andExpect(status().isBadRequest())
                .andReturn();

        String content = result.getResponse().getContentAsString();

        assertEquals(content, "{\"status\":400,\"errorInfo\":\"Bad Request: For input string: \\\"1@\\\"\"}");

        verify(employeeRegistrationService, times(0)).deleteEmployeeDetails(1L);
        verifyNoMoreInteractions(employeeRegistrationService);
    }

    @Test
    void test_deleteEmployeeDetails_returns405_whenURLIsValid() throws Exception {

        MvcResult result = this.mockMvc
                .perform(delete("/employee"))
                .andExpect(status().isMethodNotAllowed())
                .andExpect(jsonPath("$.status").value(405))
                .andExpect(jsonPath("$.errorInfo").value("Method Not Allowed"))
                .andReturn();

        String content = result.getResponse().getContentAsString();
        assertEquals(content, "{\"status\":405,\"errorInfo\":\"Method Not Allowed\"}");
        verify(employeeRegistrationService, times(0)).deleteEmployeeDetails(1L);
        verifyNoMoreInteractions(employeeRegistrationService);
    }

    @Test
    void test_addSkillsToEmployee_returns200_whenEmpIdAndSkillIdIsValid() throws Exception {

        EmployeeResponse response =
                EmployeeResponse.builder()
                        .responseStatus(HttpStatus.OK)
                        .empId("1")
                        .status(HttpStatus.OK.value())
                        .givenName("ghijk")
                        .familyName("abcdef")
                        .message("i am in")
                        .dateOfBirth("2020-02-02")
                        .build();

        when(employeeRegistrationService.addSkillsToEmployee(1L, 1L)).thenReturn(response);
        mockMvc
                .perform(put("/employee/{empId}/skills/{skillId}", "1", "1"))
                .andExpect(handler().handlerType(EmployeeController.class))
                .andExpect(handler().methodName("addSkillsToEmployee"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("i am in"))
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.dateOfBirth").value("2020-02-02"))
                .andExpect(jsonPath("$.familyName").value("abcdef"))
                .andExpect(jsonPath("$.givenName").value("ghijk"));

        verify(employeeRegistrationService, times(1)).addSkillsToEmployee(1L, 1L);
        verifyNoMoreInteractions(employeeRegistrationService);
    }

    @Test
    void test_addSkillsToEmployee_returns400_whenEmpIdIsInValid() throws Exception {

        EmployeeResponse response =
                EmployeeResponse.builder()
                        .responseStatus(HttpStatus.OK)
                        .empId("1")
                        .status(HttpStatus.OK.value())
                        .givenName("ghijk")
                        .familyName("abcdef")
                        .message("i am in")
                        .dateOfBirth("2020-02-02")
                        .build();

        when(employeeRegistrationService.addSkillsToEmployee(1L, 1L)).thenReturn(response);
        MvcResult result = this.mockMvc
                .perform(put("/employee/{empId}/skills/{skillId}", "1H", "1"))
                .andExpect(handler().handlerType(EmployeeController.class))
                .andExpect(handler().methodName("addSkillsToEmployee"))
                .andExpect(status().isBadRequest())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        assertEquals(content, "{\"status\":400,\"errorInfo\":\"Bad Request: For input string: \\\"1H\\\"\"}");

        verify(employeeRegistrationService, times(0)).addSkillsToEmployee(1L, 1L);
        verifyNoMoreInteractions(employeeRegistrationService);
    }

    @Test
    void test_addSkillsToEmployee_returns400_whenEmpIdAndSkillIdIsInValid() throws Exception {

        EmployeeResponse response =
                EmployeeResponse.builder()
                        .responseStatus(HttpStatus.OK)
                        .empId("1")
                        .status(HttpStatus.OK.value())
                        .givenName("ghijk")
                        .familyName("abcdef")
                        .message("i am in")
                        .dateOfBirth("2020-02-02")
                        .build();

        when(employeeRegistrationService.addSkillsToEmployee(1L, 1L)).thenReturn(response);
        MvcResult result = this.mockMvc
                .perform(put("/employee/{empId}/skills/{skillId}", "1'", "1D"))
                .andExpect(handler().handlerType(EmployeeController.class))
                .andExpect(handler().methodName("addSkillsToEmployee"))
                .andExpect(status().isBadRequest())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        assertEquals(content, "{\"status\":400,\"errorInfo\":\"Bad Request: For input string: \\\"1'\\\"\"}");

        verify(employeeRegistrationService, times(0)).addSkillsToEmployee(1L, 1L);
        verifyNoMoreInteractions(employeeRegistrationService);
    }

    @Test
    void test_addSkillsToEmployee_returns404_whenEmpIdAndSkillIdIsInValid() throws Exception {

        EmployeeResponse response =
                EmployeeResponse.builder()
                        .responseStatus(HttpStatus.OK)
                        .empId("1")
                        .status(HttpStatus.OK.value())
                        .givenName("ghijk")
                        .familyName("abcdef")
                        .message("i am in")
                        .dateOfBirth("2020-02-02")
                        .build();

        when(employeeRegistrationService.addSkillsToEmployee(1L, 1L)).thenReturn(response);
        MvcResult result = this.mockMvc
                .perform(put("/employee/{empId}/skills/", "1"))
                .andExpect(status().isNotFound())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        assertEquals(content, "");

        verify(employeeRegistrationService, times(0)).addSkillsToEmployee(1L, 1L);
        verifyNoMoreInteractions(employeeRegistrationService);
    }

    @Test
    void test_deleteSkillFromEmployee_returns200_whenEmpIdAndSkillIdIsValid() throws Exception {

        EmployeeResponse response =
                EmployeeResponse.builder()
                        .responseStatus(HttpStatus.OK)
                        .empId("1")
                        .status(HttpStatus.OK.value())
                        .givenName("ghijk")
                        .familyName("abcdef")
                        .message("i am in")
                        .dateOfBirth("2020-02-02")
                        .build();

        when(employeeRegistrationService.deleteSkillFromEmployee(1L, 1L)).thenReturn(response);
        mockMvc
                .perform(delete("/employee/{empId}/skills/{skillId}", "1", "1"))
                .andExpect(handler().handlerType(EmployeeController.class))
                .andExpect(handler().methodName("deleteSkillFromEmployee"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("i am in"))
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.dateOfBirth").value("2020-02-02"))
                .andExpect(jsonPath("$.familyName").value("abcdef"))
                .andExpect(jsonPath("$.givenName").value("ghijk"));

        verify(employeeRegistrationService, times(1)).deleteSkillFromEmployee(1L, 1L);
        verifyNoMoreInteractions(employeeRegistrationService);
    }

    private String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
