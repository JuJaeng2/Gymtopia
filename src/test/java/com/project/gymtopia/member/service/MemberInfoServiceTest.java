package com.project.gymtopia.member.service;

import com.project.gymtopia.exception.CustomException;
import com.project.gymtopia.exception.ErrorCode;
import com.project.gymtopia.member.data.entity.Member;
import com.project.gymtopia.member.data.model.MemberResponse;
import com.project.gymtopia.member.data.model.MemberUpdate;
import com.project.gymtopia.member.repository.MemberRepository;
import com.project.gymtopia.member.service.impl.MemberInfoServiceImpl;
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
public class MemberInfoServiceTest {

  @Mock
  private MemberRepository memberRepository;

  @Mock
  private PasswordEncoder passwordEncoder;

  @InjectMocks
  MemberInfoServiceImpl memberInfoService;

  private Member member;
  private MemberUpdate memberUpdate;

  @BeforeEach
  void setUp() {
    member = Member.builder()
        .name("홍길동")
        .email("gildong@naver.com")
        .build();

    BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
    String newPw = bCryptPasswordEncoder.encode("54321");

    memberUpdate = MemberUpdate.builder()
        .number("010-3333-4444")
        .address("제주시 어쩌구 저쩌구")
        .birth(LocalDate.parse("2020-01-01"))
        .password(newPw)
        .build();
  }

  @Test
  @DisplayName("회원 정보 가져오기")
  void getMemberInformationTest() {
    //given

    Mockito.when(memberRepository.findByEmail(Mockito.anyString())).thenReturn(
        Optional.of(member));
    //when

    MemberResponse memberResponse = memberInfoService.getMemberInformation(Mockito.anyString());

    //then
    Assertions.assertEquals("홍길동", memberResponse.getName());
    Assertions.assertEquals("gildong@naver.com", memberResponse.getEmail());
  }

  @Test
  @DisplayName("getMemberInformation-USER_NOT_FOUND")
  void getMemberInformationExceptionTest() {
    //given

    Mockito.when(memberRepository.findByEmail(Mockito.anyString())).thenReturn(Optional.empty());
    //when

    Throwable exception = Assertions.assertThrows(CustomException.class, () ->
        memberInfoService.getMemberInformation(Mockito.anyString()));

    //then
    Assertions.assertEquals(ErrorCode.MEMBER_NOT_FOUND.getMessage(), exception.getMessage());
  }

  @Test
  @DisplayName("회원 정보 수정")
  void updateInfoTest() {
    //given

    Member updatedMember = Member.builder()
        .number(memberUpdate.getNumber())
        .password(memberUpdate.getPassword())
        .birth(memberUpdate.getBirth())
        .address(memberUpdate.getAddress())
        .build();

    Mockito.when(memberRepository.findByEmail(Mockito.anyString())).thenReturn(Optional.of(member));
    Mockito.when(memberRepository.save(Mockito.any(Member.class))).thenReturn(updatedMember);
    //when

    MemberResponse memberResponse = memberInfoService.updateInfo(memberUpdate, Mockito.anyString());

    //then

    Assertions.assertEquals("010-3333-4444", memberResponse.getNumber());
    Assertions.assertEquals("제주시 어쩌구 저쩌구", memberResponse.getAddress());
  }


  @Test
  @DisplayName("회원 정보 수정-USER_NOT_FOUND")
  void updateInfo_Exception() {
    //given

    Mockito.when(memberRepository.findByEmail(Mockito.anyString())).thenReturn(Optional.empty());
    //when

    Throwable exception = Assertions.assertThrows(CustomException.class, () ->
        memberInfoService.updateInfo(memberUpdate, "test@naver.com"));

    //then
    Assertions.assertEquals(ErrorCode.MEMBER_NOT_FOUND.getMessage(), exception.getMessage());

  }

}
