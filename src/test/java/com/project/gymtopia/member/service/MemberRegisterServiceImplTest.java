package com.project.gymtopia.member.service;

import static org.junit.jupiter.api.Assertions.*;

import com.project.gymtopia.exception.CustomException;
import com.project.gymtopia.exception.ErrorCode;
import com.project.gymtopia.member.data.entity.Member;
import com.project.gymtopia.member.data.entity.Register;
import com.project.gymtopia.member.data.model.RegisterForm;
import com.project.gymtopia.member.repository.MemberRepository;
import com.project.gymtopia.member.repository.RegisterRepository;
import com.project.gymtopia.member.service.impl.MemberRegisterServiceImpl;
import com.project.gymtopia.trainer.data.entity.Trainer;
import com.project.gymtopia.trainer.repository.TrainerRepository;
import java.time.LocalDate;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MemberRegisterServiceImplTest {

  @Mock
  private RegisterRepository registerRepository;
  @Mock
  private MemberRepository memberRepository;
  @Mock
  private TrainerRepository trainerRepository;

  @InjectMocks
  private MemberRegisterServiceImpl memberRegisterService;

  private String email;
  private Member member;
  private Trainer trainer;
  private RegisterForm registerForm;

  @BeforeEach
  void setUp(){
    email = "member@naver.com";
    member = Member.builder()
        .id(1L)
        .name("Member")
        .email("member@naver.com")
        .build();
    trainer = Trainer.builder()
        .id(1L)
        .email("trainer@naver.com")
        .name("Trainer")
        .career("Test career")
        .introduction("Test introduction")
        .number("010-1111-1111")
        .build();

    LocalDate now = LocalDate.now();

    registerForm = RegisterForm.builder()
        .startDate(now)
        .endDate(now.plusMonths(3))
        .build();
  }

  @Test
  void 운동_등록요청_성공(){
    //given
    Mockito.when(memberRepository.findByEmail(Mockito.anyString()))
        .thenReturn(Optional.of(member));
    Mockito.when(trainerRepository.findById(Mockito.anyLong()))
        .thenReturn(Optional.of(trainer));

    ArgumentCaptor<Register> registerCaptor = ArgumentCaptor.forClass(Register.class);

    //when
    memberRegisterService.register(email, 1L, registerForm);

    //then
    Mockito.verify(registerRepository).save(registerCaptor.capture());
    Register savedRegister = registerCaptor.getValue();

    Assertions.assertEquals(member, savedRegister.getMember());
    Assertions.assertEquals(trainer, savedRegister.getTrainer());
    Assertions.assertFalse(savedRegister.isAcceptYn());
  }
  @Test
  void 운동_등록요청_실패1(){
    //given
    Mockito.when(memberRepository.findByEmail(Mockito.anyString()))
        .thenReturn(Optional.empty());

    //when
    Throwable exception = Assertions.assertThrows(CustomException.class,
        () -> memberRegisterService.register(email, 1L, registerForm));

    //then
    Assertions.assertEquals(ErrorCode.MEMBER_NOT_FOUND.getMessage(), exception.getMessage());
  }
  @Test
  void 운동_등록요청_실패2(){
    //given
    Mockito.when(memberRepository.findByEmail(Mockito.anyString()))
        .thenReturn(Optional.of(member));
    Mockito.when(trainerRepository.findById(Mockito.anyLong()))
        .thenReturn(Optional.empty());

    //when
    Throwable exception = Assertions.assertThrows(CustomException.class,
        () -> memberRegisterService.register(email, 1L, registerForm));

    //then
    Assertions.assertEquals(ErrorCode.TRAINER_NOT_FOUND.getMessage(), exception.getMessage());
  }

}