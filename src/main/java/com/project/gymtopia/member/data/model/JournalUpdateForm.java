package com.project.gymtopia.member.data.model;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JournalUpdateForm {

  private String title;
  private String contents;
  private List<MultipartFile> imageFileList;
  private MultipartFile videoFile;

}
