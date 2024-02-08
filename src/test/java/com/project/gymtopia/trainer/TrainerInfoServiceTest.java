package com.project.gymtopia.trainer;

import com.project.gymtopia.exception.CustomException;
import com.project.gymtopia.exception.ErrorCode;
import com.project.gymtopia.trainer.data.entity.Trainer;
import com.project.gymtopia.trainer.data.model.TrainerResponse;
import com.project.gymtopia.trainer.data.model.TrainerUpdate;
import com.project.gymtopia.trainer.repository.TrainerRepository;
import com.project.gymtopia.trainer.service.impl.TrainerInfoServiceImpl;
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

@ExtendWith(MockitoExtension.class)
public class TrainerInfoServiceTest {

  @Mock
  private TrainerRepository trainerRepository;

  @InjectMocks
  TrainerInfoServiceImpl trainerInfoService;

  private Trainer trainer;
  private TrainerUpdate trainerUpdate;
  private Trainer updatedTrainer;

  @BeforeEach
  void setUp() {
    trainer = Trainer.builder()
        .name("홍길동")
        .email("gildong@naver.com")
        .build();

    BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
    String newPw = bCryptPasswordEncoder.encode("54321");

    trainerUpdate = TrainerUpdate.builder()
        .number("010-3333-4444")
        .birth(LocalDate.parse("2020-01-01"))
        .password(newPw)
        .gymName("길동 체육관")
        .career("이런저런 우승")
        .introduction("트레이너 설명")
        .build();

    updatedTrainer = Trainer.builder()
        .number(trainerUpdate.getNumber())
        .password(trainerUpdate.getPassword())
        .birth(trainerUpdate.getBirth())
        .career(trainerUpdate.getCareer())
        .build();
  }

  @Test
  @DisplayName("트레이너 정보 가져오기")
  void getTrainerInformationTest() {
    //given

    Mockito.when(trainerRepository.findByEmail(Mockito.anyString())).thenReturn(
        Optional.of(trainer));
    //when

    TrainerResponse trainerResponse = trainerInfoService.getTrainerInformation(Mockito.anyString());

    //then
    Assertions.assertEquals("홍길동", trainerResponse.getName());
    Assertions.assertEquals("gildong@naver.com", trainerResponse.getEmail());
  }

  @Test
  @DisplayName("getTrainerInformation-USER_NOT_FOUND")
  void getTrainerInformationExceptionTest() {
    //given

    Mockito.when(trainerRepository.findByEmail(Mockito.anyString())).thenReturn(Optional.empty());
    //when

    Throwable exception = Assertions.assertThrows(CustomException.class, () ->
        trainerInfoService.getTrainerInformation(Mockito.anyString()));

    //then
    Assertions.assertEquals(ErrorCode.MEMBER_NOT_FOUND.getMessage(), exception.getMessage());
  }

  @Test
  @DisplayName("회원 정보 수정")
  void updateInfoTest() {
    //given

    Mockito.when(trainerRepository.findByEmail(Mockito.anyString()))
        .thenReturn(Optional.of(trainer));
    Mockito.when(trainerRepository.save(Mockito.any(Trainer.class))).thenReturn(updatedTrainer);
    //when

    TrainerResponse trainerResponse = trainerInfoService.updateInfo(trainerUpdate,
        Mockito.anyString());

    //then

    Assertions.assertEquals("010-3333-4444", trainerResponse.getNumber());
    Assertions.assertEquals("이런저런 우승", trainerResponse.getCareer());
  }


  @Test
  @DisplayName("트레이너 정보 수정-USER_NOT_FOUND")
  void updateInfo_Exception() {

    String email = "nothing@naver.com";
    TrainerUpdate trainerUpdate1 = new TrainerUpdate();

    Mockito.when(trainerRepository.findByEmail(email)).thenReturn(Optional.empty());

    Throwable exception = Assertions.assertThrows(CustomException.class,
        () -> trainerInfoService.updateInfo(trainerUpdate1,email));

    Assertions.assertEquals(ErrorCode.MEMBER_NOT_FOUND.getMessage(), exception.getMessage());



  }

}
