package com.project.gymtopia.member.service;

import com.project.gymtopia.member.data.model.RegisterForm;
import java.time.LocalDate;

public interface MemberRegisterService {

  void register(String email, long trainerId, RegisterForm registerForm);

  void deleteAllRegisterInBatch(LocalDate oneWeekAgo);
}
