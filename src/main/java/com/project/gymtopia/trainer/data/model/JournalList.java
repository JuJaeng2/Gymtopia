package com.project.gymtopia.trainer.data.model;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JournalList {

  private String memberName;
  private List<MemberJournalInfo> memberJournalInfoList;

}
