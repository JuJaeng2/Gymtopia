package com.project.gymtopia.member.service;

import com.project.gymtopia.common.data.model.ResponseMessage;
import com.project.gymtopia.exception.ImageUploadException;
import com.project.gymtopia.member.data.model.JournalForm;
import com.project.gymtopia.member.data.model.JournalResponse;
import java.io.IOException;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

public interface MemberJournalService {

  /**
   * daily journal 을 작성할 때 사용
   */
  boolean uploadJournal(JournalForm journalForm, String email,
      List<MultipartFile> imageMultipartFileList, MultipartFile videoMultipartFile)
      throws IOException;

  /**
   * mission journal 을 작성할 때 사용
   */
  boolean uploadJournal(JournalForm journalForm, String email, long missionId,
      List<MultipartFile> imageMultipartFileList, MultipartFile videoMultipartFile)
      throws IOException;


  JournalResponse getJournalDetail(long journalId, String email);

  boolean updateJournal(JournalForm journalForm, String email, long journalId,
      List<MultipartFile> imageFileList, MultipartFile videoFile)
      throws ImageUploadException;

  ResponseMessage deleteJournal(long journalId, String email);
}
