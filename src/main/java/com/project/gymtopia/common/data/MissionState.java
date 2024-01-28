package com.project.gymtopia.common.data;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MissionState {

  PROGRESSING("미션 진행중"),
  SUCCESS("미션 성공"),
  FAIL("미션 실패");

  private final String message;
}
