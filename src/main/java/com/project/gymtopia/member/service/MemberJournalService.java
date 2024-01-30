package com.project.gymtopia.member.service;

import com.project.gymtopia.common.data.model.ResponseMessage;
import com.project.gymtopia.member.data.model.JournalForm;
import java.io.IOException;

public interface MemberJournalService {

  ResponseMessage uploadJournal(JournalForm journalForm, String email) throws IOException;

}
