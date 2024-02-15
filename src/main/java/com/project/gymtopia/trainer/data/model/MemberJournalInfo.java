package com.project.gymtopia.trainer.data.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MemberJournalInfo {

  private long journalId;
  private String journalTitle;

}
