package com.project.gymtopia.member.controller;

import com.project.gymtopia.common.data.model.ResponseMessage;
import com.project.gymtopia.member.data.model.JournalForm;
import com.project.gymtopia.member.service.MemberJournalService;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/member/journal")
public class MemberJournalController {

  private final MemberJournalService memberJournalService;

  @PostMapping(value = "/upload", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE})
  public ResponseEntity<?> uploadJournal(
      Authentication authentication,
      @RequestPart(value = "journal", required = false) JournalForm journalForm,
      @RequestPart(value = "fileList", required = false) List<MultipartFile> fileList
  ) throws IOException {

    System.out.println("일지 제목 : " + journalForm.getTitle());
    System.out.println("일지 내용 : " + journalForm.getContents());
    System.out.println("=========== 사진 정보 ===========");
    for (MultipartFile file : fileList){
      System.out.println("이미지 파일명 : " + file.getOriginalFilename());
    }

    journalForm.setMultipartFileList(fileList);
    String email = authentication.getName();
    ResponseMessage responseMessage= memberJournalService.uploadJournal(journalForm, email);

    return ResponseEntity.ok(responseMessage);
  }

}
