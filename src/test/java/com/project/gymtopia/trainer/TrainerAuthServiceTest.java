package com.project.gymtopia.trainer;

import com.project.gymtopia.common.data.model.TokenResponse;
import com.project.gymtopia.common.data.model.UserDto;
import com.project.gymtopia.common.data.model.UserSignInForm;
import com.project.gymtopia.common.data.model.UserSignUpForm;
import com.project.gymtopia.config.jwt.JwtToken;
import com.project.gymtopia.exception.CustomException;
import com.project.gymtopia.exception.ErrorCode;
import com.project.gymtopia.trainer.data.entity.Trainer;
import com.project.gymtopia.trainer.data.model.TrainerDto;
import com.project.gymtopia.trainer.data.model.TrainerResponse;
import com.project.gymtopia.trainer.repository.TrainerRepository;
import com.project.gymtopia.trainer.service.impl.TrainerAuthServiceImpl;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;


@ExtendWith(MockitoExtension.class)
public class TrainerAuthServiceTest {

  @Mock
  private TrainerRepository trainerRepository;

  @Mock
  private JwtToken jwtToken;
  @Mock
  private PasswordEncoder passwordEncoder;

  @InjectMocks
  private TrainerAuthServiceImpl trainerAuthService;

  Trainer trainer;
  UserSignInForm signInForm;
  UserSignUpForm userSignUpForm;

  @BeforeEach
  public void setUp() {

    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
    String encodingPassword = encoder.encode("12345");

    trainer = Trainer.builder()
        .id(1L)
        .name("홍길동")
        .birth(LocalDate.parse("1998-03-26"))
        .password(encodingPassword)
        .role(List.of("TRAINER"))
        .email("gildong@naver.com")
        .number("010-1111-2222")
        .active_state("TRUE")
        .build();

    signInForm = UserSignInForm.builder()
        .email("gildong@naver.com")
        .password("12345")
        .build();

    userSignUpForm = UserSignUpForm.builder()
        .name("홍길동")
        .password("12345")
        .email("gildong@naver.com")
        .number("010-1111-2222")
        .birth(LocalDate.parse("1998-03-26"))
        .address("서울시 어쩌구로 12-2")
        .build();
  }

  @Test
  @DisplayName("로그인 인증")
  public void authenticateSuccess() {

    //given

    Mockito.when(trainerRepository.findByEmail(Mockito.anyString())).thenReturn(Optional.of(trainer));
    Mockito.when(passwordEncoder.matches(Mockito.anyString(), Mockito.anyString())).thenReturn(true);
    //when

    TrainerDto trainerDto = trainerAuthService.authenticate(signInForm.getEmail(),
        signInForm.getPassword());

    // then

    Assertions.assertEquals("홍길동", trainerDto.getName());
    Assertions.assertEquals("gildong@naver.com", trainerDto.getEmail());
    Assertions.assertEquals("TRAINER", trainerDto.getRole());

  }

  @Test
  @DisplayName("USER_NOT_FOUND Exception")
  void authenticateExceptionTest1() {
    //given

    Mockito.when(trainerRepository.findByEmail(Mockito.anyString())).thenThrow(new CustomException(
        ErrorCode.USER_NOT_FOUND));
    //when

    Throwable exception = Assertions.assertThrows(CustomException.class, () -> trainerAuthService.authenticate(
        signInForm.getEmail(), signInForm.getPassword()));

    //then

    Assertions.assertEquals(ErrorCode.USER_NOT_FOUND.getMessage(), exception.getMessage());

  }

  @Test
  @DisplayName("WRONG_PASSWORD Exception")
  void authenticateExceptionTest2() {
    //given


    Mockito.when(trainerRepository.findByEmail(Mockito.anyString())).thenReturn(
        Optional.of(trainer));
    Mockito.when(passwordEncoder.matches(Mockito.anyString(), Mockito.anyString()))
        .thenReturn(false);
    //when

    Throwable exception = Assertions.assertThrows(CustomException.class,
        () -> trainerAuthService.authenticate(
            signInForm.getEmail(), signInForm.getPassword()));

    //then

    Assertions.assertEquals(ErrorCode.WRONG_PASSWORD.getMessage(), exception.getMessage());

  }


  @Test
  @DisplayName("createToken()")
  void createTokenTest(){

    //given
    TrainerDto trainerDto = TrainerDto.builder()
        .email("gildong@naver.com")
        .id(1L)
        .name("홍길동")
        .role("TRAINER")
        .build();

    Mockito.when(jwtToken.createToken(Mockito.any(
        UserDto.class),Mockito.anyString())).thenReturn(new TokenResponse("New Token"));

    //when
    TokenResponse tokenResponse = trainerAuthService.createToken(trainerDto);
    //then

    Assertions.assertEquals("New Token", tokenResponse.getToken());

  }

  @Test
  @DisplayName("signUp()")
  void signUpTest1(){
    //given


    BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
    String encodingPassword = bCryptPasswordEncoder.encode(userSignUpForm.getPassword());

    Mockito.when(trainerRepository.existsByEmail(Mockito.anyString())).thenReturn(false);
    Mockito.when(passwordEncoder.encode(Mockito.anyString())).thenReturn(encodingPassword);
    Mockito.when(trainerRepository.save(Mockito.any())).thenReturn(trainer);
    //when

    TrainerResponse trainerResponse = trainerAuthService.signUp(userSignUpForm);
    //then

    Assertions.assertEquals(userSignUpForm.getName(), trainerResponse.getName());
    Assertions.assertEquals(userSignUpForm.getEmail(), trainerResponse.getEmail());
  }

  @Test
  @DisplayName("signUpException-REGISTERED_EMAIL")
  void signUpTest2(){
    //given

    Mockito.when(trainerRepository.existsByEmail(Mockito.anyString())).thenReturn(true);
    //when

    Throwable exception = Assertions.assertThrows(CustomException.class, () -> trainerAuthService.signUp(userSignUpForm));
    //then

    Assertions.assertEquals(ErrorCode.REGISTERED_EMAIL.getMessage(), exception.getMessage());
  }


}
