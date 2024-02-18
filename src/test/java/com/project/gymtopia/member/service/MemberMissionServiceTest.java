package com.project.gymtopia.member.service;

import com.project.gymtopia.common.data.entity.Mission;
import com.project.gymtopia.common.repository.MissionRepository;
import com.project.gymtopia.exception.CustomException;
import com.project.gymtopia.exception.ErrorCode;
import com.project.gymtopia.member.data.entity.Member;
import com.project.gymtopia.member.data.model.MissionResponse;
import com.project.gymtopia.member.repository.MemberRepository;
import com.project.gymtopia.member.service.impl.MemberMissionServiceImpl;
import com.project.gymtopia.trainer.data.entity.Trainer;
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

@ExtendWith(MockitoExtension.class)
public class MemberMissionServiceTest {

  @Mock
  private MemberRepository memberRepository;
  @Mock
  private MissionRepository missionRepository;

  @InjectMocks
  MemberMissionServiceImpl memberMissionService;

  private final String email = "gildong@naver.com";
  private Member member;
  private Trainer trainer;
  private List<Mission> missionList = new ArrayList<>();
  private Mission mission;
  @BeforeEach
  void setUp(){
    trainer = Trainer.builder()
        .id(1L)
        .name("Trainer")
        .build();

    member = Member.builder()
        .id(1L)
        .name("Member")
        .email("gildong@naver.com")
        .build();
    mission = Mission.builder()
        .id(1L)
        .title("Test Mission")
        .contents("Test mission contents")
        .trainer(trainer)
        .build();
    missionList.add(mission);
  }

  @Test
  void 모든미션_확인_성공(){
    //given
    Mockito.when(memberRepository.findByEmail(Mockito.anyString()))
        .thenReturn(Optional.of(member));
    Mockito.when(missionRepository.findAllByMember(Mockito.any(Member.class)))
        .thenReturn(Optional.of(missionList));
    //when
    List<MissionResponse> missionResponseList = memberMissionService.getAllMission(email);
    //then
    Assertions.assertEquals("Test Mission", missionResponseList.get(0).getTitle());
  }

  @Test
  void 모든미션_확인_실패1(){
    //given
    Mockito.when(memberRepository.findByEmail(Mockito.anyString()))
        .thenReturn(Optional.empty());
    //when
    Throwable exception = Assertions.assertThrows(CustomException.class,
        () -> memberMissionService.getAllMission(email));
    //then
    Assertions.assertEquals(ErrorCode.MEMBER_NOT_FOUND.getMessage(), exception.getMessage());

  }
  @Test
  void 모든미션_확인_실패2(){
    //given
    Mockito.when(memberRepository.findByEmail(Mockito.anyString()))
        .thenReturn(Optional.of(member));
    Mockito.when(missionRepository.findAllByMember(Mockito.any(Member.class)))
        .thenReturn(Optional.empty());
    //when
    Throwable exception = Assertions.assertThrows(CustomException.class,
        () -> memberMissionService.getAllMission(email));
    //then
    Assertions.assertEquals(ErrorCode.MISSION_NOT_FOUND.getMessage(), exception.getMessage());

  }

}
