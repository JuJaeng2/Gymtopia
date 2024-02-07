package com.project.gymtopia.member;

import static com.project.gymtopia.common.roles.Roles.MEMBER;

import com.project.gymtopia.common.data.model.TokenResponse;
import com.project.gymtopia.common.data.model.UserDto;
import com.project.gymtopia.common.data.model.UserSignInForm;
import com.project.gymtopia.common.data.model.UserSignUpForm;
import com.project.gymtopia.common.roles.Roles;
import com.project.gymtopia.config.jwt.JwtToken;
import com.project.gymtopia.exception.CustomException;
import com.project.gymtopia.exception.ErrorCode;
import com.project.gymtopia.member.data.entity.Member;
import com.project.gymtopia.member.data.model.MemberDto;
import com.project.gymtopia.member.data.model.MemberResponse;
import com.project.gymtopia.member.repository.MemberRepository;
import com.project.gymtopia.member.service.impl.MemberAuthServiceImpl;
import java.time.LocalDate;
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
public class MemberAuthServiceTest {

  @Mock
  private MemberRepository memberRepository;

  @Mock
  private JwtToken jwtToken;
  @Mock
  private PasswordEncoder passwordEncoder;

  @InjectMocks
  private MemberAuthServiceImpl memberAuthService;

  Member member;
  UserSignInForm signInForm;
  UserSignUpForm userSignUpForm;
  @BeforeEach
  public void setUp() {

    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
    String encodingPassword = encoder.encode("12345");

    member = Member.builder()
        .id(1L)
        .name("홍길동")
        .address("경기도 뭐시기 저시기")
        .birth(LocalDate.parse("1998-03-26"))
        .password(encodingPassword)
        .role(MEMBER)
        .email("gildong@naver.com")
        .number("010-1111-2222")
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
    Member member = Member.builder()
        .id(1L)
        .name("홍길동")
        .address("경기도 뭐시기 저시기")
        .birth(LocalDate.parse("1998-03-26"))
        .password("12345")
        .role(MEMBER)
        .email("gildong@naver.com")
        .number("010-1111-2222")
        .build();



    Mockito.when(memberRepository.findByEmail(Mockito.anyString())).thenReturn(Optional.of(member));
    Mockito.when(passwordEncoder.matches(Mockito.anyString(), Mockito.anyString()))
        .thenReturn(true);
    //when

    MemberDto memberDto = memberAuthService.authenticate(signInForm.getEmail(),
        signInForm.getPassword());
    // then

    Assertions.assertEquals("홍길동", memberDto.getName());
    Assertions.assertEquals("gildong@naver.com", memberDto.getEmail());
    Assertions.assertEquals(MEMBER, memberDto.getRole());

  }

  @Test
  @DisplayName("authenticateException-USER_NOT_FOUND")
  void authenticateExceptionTest1() {
    //given

    Mockito.when(memberRepository.findByEmail(Mockito.anyString())).thenThrow(new CustomException(
        ErrorCode.MEMBER_NOT_FOUND));
    //when

    Throwable exception = Assertions.assertThrows(CustomException.class, () -> memberAuthService.authenticate(
        signInForm.getEmail(), signInForm.getPassword()));

    //then

    Assertions.assertEquals(ErrorCode.MEMBER_NOT_FOUND.getMessage(), exception.getMessage());

  }

  @Test
  @DisplayName("authenticateException-WRONG_PASSWORD")
  void authenticateExceptionTest2() {
    //given


    Mockito.when(memberRepository.findByEmail(Mockito.anyString())).thenReturn(
        Optional.of(member));
    Mockito.when(passwordEncoder.matches(Mockito.anyString(), Mockito.anyString()))
        .thenReturn(false);
    //when

    Throwable exception = Assertions.assertThrows(CustomException.class,
        () -> memberAuthService.authenticate(
            signInForm.getEmail(), signInForm.getPassword()));

    //then

    Assertions.assertEquals(ErrorCode.WRONG_PASSWORD.getMessage(), exception.getMessage());

  }

  @Test
  @DisplayName("createToken()")
  void createTokenTest(){

    //given
    MemberDto memberDto = MemberDto.builder()
        .email("gildong@naver.com")
        .id(1L)
        .name("홍길동")
        .role(MEMBER)
        .build();

    Mockito.when(jwtToken.createToken(Mockito.any(UserDto.class),Mockito.any(Roles.class))).thenReturn(new TokenResponse("New Token"));

    //when
    TokenResponse tokenResponse = memberAuthService.createToken(memberDto);
    //then

    Assertions.assertEquals("New Token", tokenResponse.getToken());

  }

  @Test
  @DisplayName("signUp()")
  void signUpTest1(){
    //given


    BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
    String encodingPassword = bCryptPasswordEncoder.encode(userSignUpForm.getPassword());

    Mockito.when(memberRepository.existsByEmail(Mockito.anyString())).thenReturn(false);
    Mockito.when(passwordEncoder.encode(Mockito.anyString())).thenReturn(encodingPassword);
    Mockito.when(memberRepository.save(Mockito.any())).thenReturn(member);
    //when

    MemberResponse memberResponse = memberAuthService.signUp(userSignUpForm);
    //then

    Assertions.assertEquals(userSignUpForm.getName(), memberResponse.getName());
    Assertions.assertEquals(userSignUpForm.getEmail(), memberResponse.getEmail());
  }

  @Test
  @DisplayName("signUpException-REGISTERED_EMAIL")
  void signUpTest2(){
    //given

    Mockito.when(memberRepository.existsByEmail(Mockito.anyString())).thenReturn(true);
    //when

    Throwable exception = Assertions.assertThrows(CustomException.class, () -> memberAuthService.signUp(userSignUpForm));
    //then

    Assertions.assertEquals(ErrorCode.REGISTERED_EMAIL.getMessage(), exception.getMessage());
  }

}
