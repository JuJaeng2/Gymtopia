package com.project.gymtopia.member.service;

import com.project.gymtopia.member.data.model.MissionResponse;
import java.util.List;

public interface MemberMissionService {

  List<MissionResponse> getAllMission(String email);

}
