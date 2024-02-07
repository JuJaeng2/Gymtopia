package com.project.gymtopia.member.service;

import com.project.gymtopia.member.data.model.MemberResponse;
import com.project.gymtopia.member.data.model.MemberUpdate;

public interface MemberInfoService {

  MemberResponse getMemberInformation(String email);

  MemberResponse updateInfo(MemberUpdate memberUpdate, String email);
}
