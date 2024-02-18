//package com.project.gymtopia.member;
//
//import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.project.gymtopia.common.data.model.TokenResponse;
//import com.project.gymtopia.common.data.model.UserSignInForm;
//import com.project.gymtopia.common.data.model.UserSignUpForm;
//import com.project.gymtopia.config.jwt.JwtToken;
//import com.project.gymtopia.member.controller.MemberJoinController;
//import com.project.gymtopia.member.data.model.MemberDto;
//import com.project.gymtopia.member.data.model.MemberResponse;
//import com.project.gymtopia.member.service.impl.MemberAuthServiceImpl;
//import java.time.LocalDate;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.mockito.Mockito;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.http.MediaType;
//import org.springframework.security.test.context.support.WithMockUser;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.MvcResult;
//import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
//import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
//
//@WebMvcTest(MemberJoinController.class)
//public class MemberJoinControllerTest {
//
//
//  @MockBean
//  private MemberAuthServiceImpl memberAuthService;
//
//  @Autowired
//  private MockMvc mockMvc;
//
//  @Autowired
//  private ObjectMapper objectMapper;
//
//  @MockBean
//  private JwtToken jwtToken;
//
//  @BeforeEach
//  void setUp() {
//
//  }
//
//  @Test
//  @DisplayName("memberSignUp()")
//  @WithMockUser(username = "홍길동", roles = {"MEMBER"})
//  void memberSignUpTest() throws Exception {
//
//    //given
//    UserSignUpForm signUpForm = UserSignUpForm.builder()
//        .name("홍길동")
//        .password("12345")
//        .email("gildong@naver.com")
//        .number("010-1111-2222")
//        .birth(LocalDate.parse("1998-03-26"))
//        .address("서울시 어쩌구 저쩌구")
//        .build();
//
//    Mockito.when(memberAuthService.signUp(Mockito.any(UserSignUpForm.class))).thenReturn(
//        MemberResponse.builder()
//            .name("홍길동")
//            .email("gildong@naver.com")
//            .number("010-1111-2222")
//            .birth(LocalDate.parse("1998-03-26"))
//            .address("서울시 어쩌구 저쩌구")
//            .build());
//    // when
//
//    // then
//    MvcResult mvcResult = mockMvc.perform(
//            MockMvcRequestBuilders.post("/signUp/member").with(csrf())
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(objectMapper.writeValueAsString(signUpForm)))
//        .andExpect(MockMvcResultMatchers.status().isOk())
//        .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("홍길동"))
//        .andReturn();
//
//  }
//
//  @Test
//  @DisplayName("memberSignIn()")
//  @WithMockUser(username = "홍길동", roles = {"MEMBER"})
//  void memberSignInTest() throws Exception {
//    //given
//    UserSignInForm signInForm = UserSignInForm.builder()
//        .email("gildong@naver.com")
//        .password("12345")
//        .build();
//
//    Mockito.when(memberAuthService.authenticate(Mockito.anyString(), Mockito.anyString()))
//        .thenReturn(MemberDto.builder()
//            .name("")
//            .build());
//
//    Mockito.when(memberAuthService.createToken(Mockito.any(MemberDto.class))).thenReturn(
//        TokenResponse.builder()
//            .token("New Token")
//            .build());
//    //when
//
//    //then
//    mockMvc.perform(
//            MockMvcRequestBuilders.post("/signIn/member").with(csrf())
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(objectMapper.writeValueAsString(signInForm)))
//        .andExpect(MockMvcResultMatchers.status().isOk())
//        .andExpect(MockMvcResultMatchers.jsonPath("$.token").value("New Token"));
//  }
//
//
//}
