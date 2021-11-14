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
import uk.nhs.nhsbsa.employeeskills.request.SkillsRegistrationRequest;
import uk.nhs.nhsbsa.employeeskills.response.SkillsResponse;
import uk.nhs.nhsbsa.employeeskills.service.SkillRegistrationService;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class SkillControllerTest {

  @Autowired private MockMvc mockMvc;

  @MockBean private SkillRegistrationService skillRegistrationService;

  @Test
  void test_registerSkills_returns200_whenRequestObjectIsValid() throws Exception {
    SkillsRegistrationRequest request =
        SkillsRegistrationRequest.builder().skill("singing").level("Working").build();

    SkillsResponse response =
        SkillsResponse.builder()
            .responseStatus(HttpStatus.CREATED)
            .skillId("1")
            .status(HttpStatus.CREATED.value())
            .skill("singing")
            .level("Working")
            .message("i am in")
            .build();

    when(skillRegistrationService.registerSkills(request)).thenReturn(response);
    mockMvc
        .perform(
            post("/skills")
                .header("Content-Type", "application/json")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(request)))
        .andExpect(handler().handlerType(SkillController.class))
        .andExpect(handler().methodName("registerSkills"))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.message").value("i am in"))
        .andExpect(jsonPath("$.status").value(201))
        .andExpect(jsonPath("$.skill").value("singing"))
        .andExpect(jsonPath("$.level").value("Working"));

    verify(skillRegistrationService, times(1)).registerSkills(request);
    verifyNoMoreInteractions(skillRegistrationService);
  }

  @Test
  void test_fetchRegisteredSkill_returns200_whenSkillIdIsValid() throws Exception {

    SkillsResponse response =
        SkillsResponse.builder()
            .responseStatus(HttpStatus.OK)
            .skillId("1")
            .status(HttpStatus.OK.value())
            .skill("singing")
            .level("Working")
            .message("i am in")
            .build();

    when(skillRegistrationService.fetchRegisteredSkill(1L)).thenReturn(response);
    mockMvc
        .perform(get("/skills/{skillId}", "1"))
        .andExpect(handler().handlerType(SkillController.class))
        .andExpect(handler().methodName("fetchRegisteredSkill"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.message").value("i am in"))
        .andExpect(jsonPath("$.status").value(200))
        .andExpect(jsonPath("$.skill").value("singing"))
        .andExpect(jsonPath("$.skillId").value("1"))
        .andExpect(jsonPath("$.level").value("Working"));

    verify(skillRegistrationService, times(1)).fetchRegisteredSkill(1L);
    verifyNoMoreInteractions(skillRegistrationService);
  }

  @Test
  void test_deleteRegisteredSkill_returns200_whenSkillIdIsValid() throws Exception {

    SkillsResponse response =
        SkillsResponse.builder()
            .responseStatus(HttpStatus.OK)
            .skillId("1")
            .status(HttpStatus.OK.value())
            .skill("singing")
            .level("Working")
            .message("i am in")
            .build();

    when(skillRegistrationService.deleteRegisteredSkill(1L)).thenReturn(response);
    mockMvc
        .perform(delete("/skills/{skillId}", "1"))
        .andExpect(handler().handlerType(SkillController.class))
        .andExpect(handler().methodName("deleteRegisteredSkill"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.message").value("i am in"))
        .andExpect(jsonPath("$.status").value(200));

    verify(skillRegistrationService, times(1)).deleteRegisteredSkill(1L);
    verifyNoMoreInteractions(skillRegistrationService);
  }

  @Test
  void test_updateRegisteredSkills_returns200_whenRequestObjectIsValid() throws Exception {
    SkillsRegistrationRequest request =
        SkillsRegistrationRequest.builder().skill("singing").level("Working").build();

    SkillsResponse response =
        SkillsResponse.builder()
            .responseStatus(HttpStatus.OK)
            .skillId("1")
            .status(HttpStatus.OK.value())
            .skill("singing")
            .level("Working")
            .message("i am in")
            .build();

    when(skillRegistrationService.updateRegisteredSkills(request, 1L)).thenReturn(response);
    mockMvc
        .perform(
            put("/skills/{skillId}", "1")
                .header("Content-Type", "application/json")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(request)))
        .andExpect(handler().handlerType(SkillController.class))
        .andExpect(handler().methodName("updateRegisteredSkills"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.message").value("i am in"))
        .andExpect(jsonPath("$.status").value(200))
        .andExpect(jsonPath("$.skill").value("singing"))
        .andExpect(jsonPath("$.level").value("Working"));

    verify(skillRegistrationService, times(1)).updateRegisteredSkills(request, 1L);
    verifyNoMoreInteractions(skillRegistrationService);
  }

  private String asJsonString(final Object obj) {
    try {
      return new ObjectMapper().writeValueAsString(obj);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
