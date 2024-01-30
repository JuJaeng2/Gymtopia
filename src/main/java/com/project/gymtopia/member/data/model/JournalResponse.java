package com.project.gymtopia.member.data.model;

import com.project.gymtopia.common.data.JournalType;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JournalResponse {

  private String title;
  private String contents;
  private JournalType journalType;
  private String missionTitle;
  private LocalDateTime createDateTime;

}
