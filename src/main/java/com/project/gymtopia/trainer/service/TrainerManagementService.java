package com.project.gymtopia.trainer.service;

import com.project.gymtopia.member.data.model.JournalResponse;
import com.project.gymtopia.trainer.data.model.FeedbackForm;
import com.project.gymtopia.trainer.data.model.JournalList;
import com.project.gymtopia.trainer.data.model.MemberListResponse;
import com.project.gymtopia.trainer.data.model.MissionForm;
import com.project.gymtopia.trainer.data.model.RegisterManagement;

public interface TrainerManagementService {

  MemberListResponse getMemberInfo(String email);

  JournalList getMissionJournal(String email, long memberId);


  JournalResponse getJournalInfo(String email, long memberId, long journalId);

  void writeFeedback(FeedbackForm feedBackForm, String email, long journalId);

  void updateFeedback(FeedbackForm feedbackForm, String email, long journalId);

  boolean deleteFeedback(String email, long journalId);

  void giveMission(MissionForm missionForm, String email, long memberId);

  boolean manageRegister(String email, long registerId, RegisterManagement registerManagement);
}
