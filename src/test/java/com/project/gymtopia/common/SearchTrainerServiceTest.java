package com.project.gymtopia.common;

import com.project.gymtopia.common.data.model.TrainerSearchResponse;
import com.project.gymtopia.common.service.impl.SearchTrainerServiceImpl;
import com.project.gymtopia.exception.CustomException;
import com.project.gymtopia.exception.ErrorCode;
import com.project.gymtopia.trainer.data.entity.Trainer;
import com.project.gymtopia.trainer.data.model.TrainerProfile;
import com.project.gymtopia.trainer.repository.TrainerRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
public class SearchTrainerServiceTest {

  @Mock
  private TrainerRepository trainerRepository;

  @InjectMocks
  private SearchTrainerServiceImpl searchTrainerService;

  Trainer trainer1;
  Trainer trainer2;
  Trainer trainer3;
  Trainer trainer4;
  Trainer trainer5;
  Trainer trainer6;
  Trainer trainer7;
  Trainer trainer8;
  Trainer trainer9;

  @BeforeEach
  void setUp(){
    trainer1 = Trainer.builder()
        .id(1L)
        .email("trainer@naver.com")
        .name("Trainer")
        .career("Test career")
        .introduction("Test introduction")
        .number("010-1111-1111")
        .build();
    trainer2 = Trainer.builder()
        .id(2L)
        .email("trainer2@naver.com")
        .name("Trainer2")
        .career("Test career2")
        .introduction("Test introduction2")
        .number("010-2222-2222")
        .build();
    trainer3 = Trainer.builder()
        .id(3L)
        .email("trainer3@naver.com")
        .name("Trainer3")
        .career("Test career3")
        .introduction("Test introduction3")
        .number("010-3333-3333")
        .build();
    trainer4 = Trainer.builder()
        .id(4L)
        .email("trainer4@naver.com")
        .name("Trainer")
        .career("Test career")
        .introduction("Test introduction")
        .number("010-1111-1111")
        .build();
    trainer5 = Trainer.builder()
        .id(5L)
        .email("trainer5@naver.com")
        .name("Trainer2")
        .career("Test career2")
        .introduction("Test introduction2")
        .number("010-2222-2222")
        .build();
    trainer6 = Trainer.builder()
        .id(6L)
        .email("trainer6@naver.com")
        .name("Trainer6")
        .career("Test career6")
        .introduction("Test introduction6")
        .number("010-3333-3333")
        .build();
    trainer7 = Trainer.builder()
        .id(7L)
        .email("traine7@naver.com")
        .name("Trainer7")
        .career("Test career7")
        .introduction("Test introduction7")
        .number("010-1111-1111")
        .build();
    trainer8 = Trainer.builder()
        .id(8L)
        .email("trainer8@naver.com")
        .name("Trainer8")
        .career("Test career2")
        .introduction("Test introduction2")
        .number("010-2222-2222")
        .build();
    trainer9 = Trainer.builder()
        .id(9L)
        .email("trainer9@naver.com")
        .name("Trainer9")
        .career("Test career3")
        .introduction("Test introduction3")
        .number("010-3333-3333")
        .build();

  }

  @Test
  void 특정트레이너_정보_가져오기_성공(){
    //given
    Mockito.when(trainerRepository.findById(Mockito.anyLong()))
        .thenReturn(Optional.of(trainer1));

    //when
    TrainerProfile trainerProfile = searchTrainerService.getTrainerInfo(1L);
    //then
    Assertions.assertEquals(trainer1.getName(), trainerProfile.getTrainerName());
    Assertions.assertEquals(trainer1.getCareer(), trainerProfile.getCareer());
    Assertions.assertEquals(trainer1.getEmail(), trainerProfile.getEmail());
  }
  @Test
  void 특정트레이너_정보_가져오기_실패(){
    //given
    Mockito.when(trainerRepository.findById(Mockito.anyLong()))
        .thenReturn(Optional.empty());

    //when
    Throwable exception = Assertions.assertThrows(CustomException.class,
        () -> searchTrainerService.getTrainerInfo(1L));
    //then
    Assertions.assertEquals(ErrorCode.TRAINER_NOT_FOUND.getMessage(), exception.getMessage());
  }

  @Test
  void 모든_트레이너_검색_성공() {
    //given
    Pageable pageable = PageRequest.of(2, 4);
    List<Trainer> trainerList = new ArrayList<>();
    trainerList.add(trainer2);
    trainerList.add(trainer1);
    trainerList.add(trainer3);
    trainerList.add(trainer4);
    trainerList.add(trainer5);
    trainerList.add(trainer6);
    trainerList.add(trainer7);
    trainerList.add(trainer8);
    trainerList.add(trainer9);

    Page<Trainer> trainerPage = new PageImpl<>(List.of(), pageable, trainerList.size());

    Mockito.when(trainerRepository.findAll(pageable))
        .thenReturn(trainerPage);
    //when
    Page<TrainerSearchResponse> responsePage = searchTrainerService.searchTrainer(pageable);
    //then
    Assertions.assertEquals(3, responsePage.getTotalPages());
  }
}
