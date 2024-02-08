package com.project.gymtopia.member.data.model;

import com.project.gymtopia.common.data.JournalType;
import com.project.gymtopia.common.data.entity.Mission;
import com.project.gymtopia.member.data.entity.Journal;
import com.project.gymtopia.trainer.data.model.FeedBackDto;
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
  private Mission mission;
  private LocalDateTime createDateTime;
  private MediaResponse mediaResponse;
  private FeedBackDto feedBackDto;

  public static JournalResponse from(Journal journal, FeedBackDto feedBackDto,
      MediaResponse mediaResponse) {
    return JournalResponse.builder()
        .title(journal.getTitle())
        .contents(journal.getContents())
        .journalType(journal.getType())
        .mission(journal.getMission())
        .feedBackDto(feedBackDto)
        .mediaResponse(mediaResponse)
        .createDateTime(journal.getCreateDateTime())
        .build();
  }

}
