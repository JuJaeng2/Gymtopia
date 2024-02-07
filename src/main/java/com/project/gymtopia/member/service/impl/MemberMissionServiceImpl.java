package com.project.gymtopia.member.service.impl;

import com.project.gymtopia.common.data.entity.Mission;
import com.project.gymtopia.common.repository.MissionRepository;
import com.project.gymtopia.exception.CustomException;
import com.project.gymtopia.exception.ErrorCode;
import com.project.gymtopia.member.data.entity.Member;
import com.project.gymtopia.member.data.model.MissionResponse;
import com.project.gymtopia.member.repository.MemberRepository;
import com.project.gymtopia.member.service.MemberMissionService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberMissionServiceImpl implements MemberMissionService {

  private final MemberRepository memberRepository;
  private final MissionRepository missionRepository;

  @Override
  public List<MissionResponse> getAllMission(String email) {

    Member member = memberRepository.findByEmail(email)
        .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

    List<Mission> missionList = missionRepository.findAllByMember(member)
        .orElseThrow(() -> new CustomException(ErrorCode.MISSION_NOT_FOUND));

    return missionList.stream().map(mission -> MissionResponse.builder()
        .title(mission.getTitle())
        .contents(mission.getContents())
        .trainerName(mission.getTrainer().getName())
        .missionState(mission.getState())
        .startDateTime(mission.getCreateDateTime())
        .expirationDateTime(mission.getExpirationDateTime())
        .build())
        .toList();
  }
}
