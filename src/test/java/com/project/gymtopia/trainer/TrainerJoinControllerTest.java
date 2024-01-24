package com.project.gymtopia.trainer;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.gymtopia.common.data.model.TokenResponse;
import com.project.gymtopia.common.data.model.UserSignInForm;
import com.project.gymtopia.common.data.model.UserSignUpForm;
import com.project.gymtopia.config.jwt.JwtToken;
import com.project.gymtopia.trainer.controller.TrainerJoinController;
import com.project.gymtopia.trainer.data.model.TrainerDto;
import com.project.gymtopia.trainer.data.model.TrainerResponse;
import com.project.gymtopia.trainer.service.impl.TrainerAuthServiceImpl;
import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@WebMvcTest(TrainerJoinController.class)
public class TrainerJoinControllerTest {


  @MockBean
  private TrainerAuthServiceImpl trainerAuthService;

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @MockBean
  private JwtToken jwtToken;

  @BeforeEach
  void setUp() {

  }

  @Test
  @DisplayName("memberSignUp()")
  @WithMockUser(username = "홍길동", roles = {"TRAINER"})
  void trainerSignUpTest() throws Exception {

    //given
    UserSignUpForm signUpForm = UserSignUpForm.builder()
        .name("홍길동")
        .password("12345")
        .email("gildong@naver.com")
        .number("010-1111-2222")
        .birth(LocalDate.parse("1998-03-26"))
        .address("서울시 어쩌구 저쩌구")
        .build();

    Mockito.when(trainerAuthService.signUp(Mockito.any(UserSignUpForm.class))).thenReturn(
        TrainerResponse.builder()
            .name("홍길동")
            .email("gildong@naver.com")
            .number("010-1111-2222")
            .build());
    // when

    // then
    mockMvc.perform(
            MockMvcRequestBuilders.post("/trainer/signUp").with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(signUpForm)))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("홍길동"))
        .andReturn();

  }

  @Test
  @DisplayName("memberSignIn()")
  @WithMockUser(username = "홍길동", roles = {"TRAINER"})
  void trainerSignInTest() throws Exception {
    //given
    UserSignInForm signInForm = UserSignInForm.builder()
        .email("gildong@naver.com")
        .password("12345")
        .build();

    Mockito.when(trainerAuthService.authenticate(Mockito.anyString(), Mockito.anyString()))
        .thenReturn(TrainerDto.builder()
            .name("")
            .build());

    Mockito.when(trainerAuthService.createToken(Mockito.any(TrainerDto.class))).thenReturn(
        TokenResponse.builder()
            .token("New Token")
            .build());
    //when

    //then
    mockMvc.perform(
            MockMvcRequestBuilders.post("/trainer/signIn").with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(signInForm)))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.token").value("New Token"));
  }


}
